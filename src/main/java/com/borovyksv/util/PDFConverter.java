package com.borovyksv.util;

import com.borovyksv.mongo.observer.Observable;
import com.borovyksv.mongo.observer.Observer;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.util.SmartPdfSplitter;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFConverter implements Observable{
  private static final Logger LOGGER = Logger.getLogger(PDFConverter.class.getName());
  private static final int N_THREADS = 10;
  private static final int MESSAGES_TO_LOG = 100;
  private static final int PERCENT_OF_CONVERTED_PAGES_TO_NOTIFY_OBSERVERS = 5;

  private java.util.List<Observer<PDFConverter>> observers = new ArrayList<>();
  private java.util.List<String> errors = new ArrayList<>();

  private int pagesProgress;
  private int imagesProgress;
  private int textProgress;

  private int IMAGE_DPI = 300;
  private float IMAGE_COMPRESSION = 0.8f;
  private String IMAGE_FORMAT = "png";
  private int DIMENSIONS_DIVIDER = 2;
  private boolean isScanned = true;
  private boolean isConverted;


  private int numberOfPages = 0;
  private int numberOfPagesToNotifyObervers=0;



  private String pdfFileDirectory;
  private String pdfFileName;
  private File file;
  private String resultFolder;
  private String resultFolderPDF;
  private String resultFolderIMG;
  private Map<Integer, String> textPages = new HashMap<>();


  /**
   *  While converter initializing, constructor creates folders to store converted files
   *  in the same destination where input file exists
   * */
  protected PDFConverter(String absolutePath) {
    this.pdfFileDirectory = absolutePath;
    this.file = new File(absolutePath);
    this.pdfFileName = this.file.getName();
    this.resultFolder = file.getParent() + File.separator + file.getName().substring(0, file.getName().length() - 4) + "_parsed" + File.separator;
    this.resultFolderPDF = resultFolder + "PDF" + File.separator;
    this.resultFolderIMG = resultFolder + "IMG" + File.separator;
    new File(resultFolder).mkdir();
    new File(resultFolderPDF).mkdir();
    new File(resultFolderIMG).mkdir();
    initConstants();

    LOGGER.log(Level.INFO, String.format("PDFConverter for %s file initialized", absolutePath));
  }

  public void convert() {
    Instant start = Instant.now();

    notifyAllObservers();

    saveBookmarks();
    savePages();
    saveImagesAndText();

    isConverted = isSuccessfullyConverted();

    notifyAllObservers();

    Instant end = Instant.now();
    LOGGER.log(Level.INFO, String.format("%nConversion time is %s ", Duration.between(start, end)));

  }

  private boolean isSuccessfullyConverted() {
    return pagesProgress == 100
      && imagesProgress == 100
      && textProgress == 100
      && errors.size() == 0;
  }

  public void saveImagesAndText() {
    LOGGER.log(Level.INFO, String.format("%s is %s pdf", pdfFileName, isScanned ? "scanned" : "text format"));

    if (isScanned) {
      saveImagesAndTextFromScannedPDF();
    } else {
      saveImages();
      saveText();
    }
  }


  public void savePages() {
    PdfReader reader = null;
    try {
      reader = new PdfReader(pdfFileDirectory);
      SmartPdfSplitter splitter = new SmartPdfSplitter(reader);
      int pageNumber = 1;
      while (splitter.hasMorePages()) {
        splitter.split(new FileOutputStream(resultFolderPDF + pageNumber + ".pdf"), 200000);

        // percent(%) of converted pages
        pagesProgress = getProgressValue(numberOfPages, pageNumber, pagesProgress);

        if (isNotifiable(pageNumber)) notifyAllObservers();

        if (pageNumber % MESSAGES_TO_LOG == 0) LOGGER.log(Level.INFO, String.format("%d.pdf saved", pageNumber));

        pageNumber++;
      }
    } catch (IOException | DocumentException e) {
      LogErrorAndNotifyObservers(e);
    } finally {if (reader != null) {reader.close();}}
  }

  public void saveText() {
    try (PDDocument doc = PDDocument.load(file)) {
      Splitter splitter = new Splitter();
      java.util.List<PDDocument> pages = splitter.split(doc);

      int counter = 1;

      for (PDDocument page : pages) {
        try {
          int pageNumber = counter++;

          String extract = new PDFTextStripper().getText(page);

          String textMark = getTextMark();

          String result =  textMark + extract;

          textPages.put(pageNumber, result);

          // percent(%) of converted text pages
          textProgress = getProgressValue(numberOfPages, pageNumber, textProgress);

          if (isNotifiable(pageNumber)) notifyAllObservers();

          if (pageNumber % MESSAGES_TO_LOG == 0) LOGGER.log(Level.INFO, String.format("%d.txt saved", pageNumber));
        } catch (IOException e) {
          LogErrorAndNotifyObservers(e);
        } finally {
          try {
            if (page != null) page.close();
          } catch (IOException e) {
            LogErrorAndNotifyObservers(e);
          }
        }
      }
    } catch (IOException e) {
      LogErrorAndNotifyObservers(e);}
  }



  public void saveImages() {

    int numberOfPages = getNumberOfPages();

    java.util.List<Integer> pageNumberOfUnhandledImages = new ArrayList<>();

    for (int startPage = 1; startPage <= numberOfPages; startPage += 100) {

      // document must be reloaded every 100 pages to prevent memory leaks
      try (PDDocument document = PDDocument.load(file)) {
        int endPage = (startPage + 99) < numberOfPages ? startPage + 99 : numberOfPages;

        PDFRenderer pdfRenderer = new PDFRenderer(document);

        AtomicInteger counter = new AtomicInteger(startPage);

        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);

        for (int i = startPage; i <= endPage; i++) {
          executorService.execute(() -> {
            try {

              int pageNumber = counter.getAndIncrement();

              BufferedImage image = null;
                try {

                  image = getImage(pdfRenderer, pageNumber);

                  saveImage(pageNumber, image);

                } catch (OutOfMemoryError oome) {
                  LOGGER.log(Level.INFO, String.format("%d%s caused OutOfMemory, retrying to convert in another tread", pageNumber, "." + IMAGE_FORMAT));
                  pageNumberOfUnhandledImages.add(pageNumber);
                }


              // percent(%) of converted images
              imagesProgress = getProgressValue(numberOfPages, pageNumber, imagesProgress);
              if (isNotifiable(pageNumber)) notifyAllObservers();

            } catch (Exception e) {
              LogErrorAndNotifyObservers(e);
            }
          });
        }
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.MINUTES);

      } catch (IOException | InterruptedException e) {
        LogErrorAndNotifyObservers(e);
      }
    }

    if(pageNumberOfUnhandledImages.size()>0) saveUnhandledImages(pageNumberOfUnhandledImages);
  }

  private void saveUnhandledImages(java.util.List<Integer> pageNumberOfUnhandledImages) {
    try (PDDocument document = PDDocument.load(file)) {

      PDFRenderer pdfRenderer = new PDFRenderer(document);

      ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);

      for (Integer page: pageNumberOfUnhandledImages) {
        executorService.execute(() -> {
          try {

            int pageNumber = page;
            pageNumberOfUnhandledImages.remove(page);

            BufferedImage image = null;

            try {

              image = getImage(pdfRenderer, pageNumber);

              saveImage(pageNumber, image);

            } catch (OutOfMemoryError oome) {
              LOGGER.log(Level.INFO, String.format("%d%s caused OutOfMemory, retrying to convert in another tread", pageNumber, "." + IMAGE_FORMAT));
              pageNumberOfUnhandledImages.add(pageNumber);
            }


          } catch (Exception e) {
            LogErrorAndNotifyObservers(e);
          }
        });
      }
      executorService.shutdown();
      executorService.awaitTermination(30, TimeUnit.MINUTES);

    } catch (IOException | InterruptedException e) {
      LogErrorAndNotifyObservers(e);
    }

    if(pageNumberOfUnhandledImages.size()>0) saveUnhandledImages(pageNumberOfUnhandledImages);
  }

  private int getProgressValue(int numberOfPages, int pageNumber, int currentValue) {
    int result = (100 * pageNumber / numberOfPages);
    result = result <= currentValue ? currentValue : result;
    return result;
  }

  /**
   * This method combines two another methods (@saveTextFromScannedPDF() and @saveImage()) within,
   * to optimize resource consuming (BufferedImage.class) needed for execution
   * */
  private void saveImagesAndTextFromScannedPDF() {

    int numberOfPages = getNumberOfPages();
    java.util.List<Integer> pageNumberOfUnhandledImages = new ArrayList<>();

    LOGGER.log(Level.INFO, String.format("Starting OCR to %s ", pdfFileName));


    for (int startPage = 1; startPage <= numberOfPages; startPage += 100)   {

      // document must be reloaded every 100 pages to prevent memory leaks
      try (PDDocument document = PDDocument.load(file)) {
        int endPage = (startPage + 99) < numberOfPages ? startPage + 99 : numberOfPages;

        PDFRenderer pdfRenderer = new PDFRenderer(document);

        AtomicInteger counter = new AtomicInteger(startPage);

        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);

        for (int i = startPage; i <= endPage; i++) {
          executorService.execute(() -> {
            try {
              int pageNumber = counter.getAndIncrement();

              BufferedImage image = null;
              try {
                image = getImage(pdfRenderer, pageNumber);

                saveTextFromScannedPDF(pageNumber, image);

                saveImage(pageNumber, image);
              } catch (OutOfMemoryError oome) {
                LOGGER.log(Level.INFO, String.format("%d%s caused OutOfMemory, retrying to convert in another tread", pageNumber, "." + IMAGE_FORMAT));
                pageNumberOfUnhandledImages.add(pageNumber);
                image.flush();
              }

              // percent(%) of converted images and text pages
              imagesProgress = textProgress = getProgressValue(numberOfPages, pageNumber, textProgress);

              if (isNotifiable(pageNumber)) notifyAllObservers();

            } catch (Exception e) {
              LogErrorAndNotifyObservers(e);
            }
          });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);

      } catch (IOException | InterruptedException e) {
        LogErrorAndNotifyObservers(e);
      }
    }
    if(pageNumberOfUnhandledImages.size()>0) saveUnhandledImagesAndText(pageNumberOfUnhandledImages);

  }

  private void saveUnhandledImagesAndText(List<Integer> pageNumberOfUnhandledImages) {
    try (PDDocument document = PDDocument.load(file)) {

      PDFRenderer pdfRenderer = new PDFRenderer(document);

      ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);

      for (Integer page: pageNumberOfUnhandledImages) {
        executorService.execute(() -> {
          try {

            int pageNumber = page;
            pageNumberOfUnhandledImages.remove(page);

            BufferedImage image = null;

            try {

              image = getImage(pdfRenderer, pageNumber);

              saveTextFromScannedPDF(pageNumber, image);

              saveImage(pageNumber, image);

            } catch (OutOfMemoryError oome) {
              LOGGER.log(Level.INFO, String.format("%d%s caused OutOfMemory, retrying to convert in another tread", pageNumber, "." + IMAGE_FORMAT));
              pageNumberOfUnhandledImages.add(pageNumber);
            }

          } catch (Exception e) {
            LogErrorAndNotifyObservers(e);
          }
        });
      }
      executorService.shutdown();
      executorService.awaitTermination(30, TimeUnit.MINUTES);

    } catch (IOException | InterruptedException e) {
      LogErrorAndNotifyObservers(e);
    }

    if(pageNumberOfUnhandledImages.size()>0) saveUnhandledImages(pageNumberOfUnhandledImages);
  }

//    private void saveTextToFile(int pageNumber, BufferedImage bufferedImage) {
//        Tesseract tessInst = new Tesseract();
//
//        try (PrintWriter out = new PrintWriter(resultFolderTXT + pageNumber + ".txt")) {
//            String result = tessInst.doOCR(bufferedImage);
//            String filteredResult = textFilter(result);
//            out.println(filteredResult);
//            LOGGER.log(Level.INFO, String.format("%d.txt saved", pageNumber));
//
//        } catch (TesseractException | FileNotFoundException e) {
//            LOGGER.log(Level.SEVERE, "Exception occur", e);
//        }
//    }

  private void saveTextFromScannedPDF(int pageNumber, BufferedImage bufferedImage) {
    Tesseract tessInst = new Tesseract();

    try {
      String result = tessInst.doOCR(bufferedImage);
      String filteredResult = textFilter(result);

      textPages.put(pageNumber, filteredResult);

      if (pageNumber % MESSAGES_TO_LOG == 0) LOGGER.log(Level.INFO, String.format("%d.txt saved", pageNumber));
    } catch (TesseractException e) {
      LogErrorAndNotifyObservers(e);
    }

  }


  private BufferedImage getImage(PDFRenderer renderer, int page) throws IOException {

    return renderer.renderImageWithDPI(page - 1, IMAGE_DPI, ImageType.RGB);

  }

  private void saveImage(int pageNumber, BufferedImage bim) throws IOException {
    File file = new File(resultFolderIMG + pageNumber + "." + IMAGE_FORMAT);
    FileOutputStream output = new FileOutputStream(file);

    Image tmp = bim.getScaledInstance(bim.getWidth() / DIMENSIONS_DIVIDER, bim.getHeight() / DIMENSIONS_DIVIDER, Image.SCALE_SMOOTH);
    BufferedImage dimg = new BufferedImage(bim.getWidth() / DIMENSIONS_DIVIDER, bim.getHeight() / DIMENSIONS_DIVIDER, BufferedImage.TYPE_BYTE_BINARY);

    Graphics2D g2d = dimg.createGraphics();
    g2d.drawImage(tmp, 0, 0, null);


    ImageIOUtil.writeImage(dimg, IMAGE_FORMAT, output, IMAGE_DPI, IMAGE_COMPRESSION);
    g2d.dispose();
    if (pageNumber % MESSAGES_TO_LOG == 0) LOGGER.log(Level.INFO, String.format("%d%s saved", pageNumber, "." + IMAGE_FORMAT));

  }

  private String textFilter(String input) {
    String group = "[=;,_\\-/\\.\\\\\\\"\\'@~]";
    String pattern = String.format("(\\D)\\1{2,}?|[^\\u0000-\\u007F\\u00b0\\n\\r\\tВ]|\\s{3,}?|%1$s{3,}|%1$s+ %1$s+|( .{1,2} .{1,2} )+", group);
    //delete garbage from whole text
    input = input.replaceAll(pattern, "");

    //Filter short lines from garbage
    StringBuilder sb = new StringBuilder();
    sb.append(getTextMark()).append("\n");
    for (String s : input.split("\n")) {
      Matcher m = Pattern.compile("[=;:_\\-/\\\\\"'@~!+,\\|\\.1%\\*\\$]").matcher(s);
      int matches = 0;
      while (m.find()) matches++;
      if (s.matches("^\\w{4,}[:\\.,]?$") || (matches < 3 && s.length() > 6) || s.length() > 20) {
        sb.append(s).append("\n");
      }
    }
    return sb.toString();
  }

  //converts fileName to indexable string
  private String getTextMark() {
    return pdfFileName.substring(0, pdfFileName.indexOf(".pdf")).replaceAll("_", " ") + "\n";
  }

  public boolean saveBookmarks() {
    try (PDDocument document = PDDocument.load(file)) {
      PDDocumentOutline outline = document.getDocumentCatalog().getDocumentOutline();
      try (PrintWriter bookmarkWriter = new PrintWriter(resultFolder + "Bookmarks.html")) {

        LOGGER.log(Level.INFO, String.format("%sBookmarks.html created", resultFolder));
        initializeBookmarkHTMLDocument(bookmarkWriter);

        if (outline == null) {

          LOGGER.log(Level.INFO, "PDF file does not have Bookmarks, writing pagelist");
          writePageList(bookmarkWriter);
          return false;

        } else {
          //writing to HTML
          LOGGER.log(Level.INFO, "writing bookmarks");
          writeBookmark(outline, "", document, bookmarkWriter);

        }

        terminateBookmarkHTMLDocument(bookmarkWriter);

      } catch (IOException e) {
        LogErrorAndNotifyObservers(e);}
    } catch (Exception e) {
      LogErrorAndNotifyObservers(e);}

    LOGGER.log(Level.INFO, "Bookmarks saved");
    return true;
  }



  private void initializeBookmarkHTMLDocument(PrintWriter bookmarkWriter) {
    bookmarkWriter.print("<HTML>\n" +
      "<HEAD>\n" +
      "<TITLE>"+ pdfFileName+"</TITLE>\n" +
      "<style>\n" +
      "        table {\n" +
      "            border-collapse: collapse;\n" +
      "            margin-left:auto; \n" +
      "            margin-right:auto;" +
      "        }\n" +
      "        h1 {\n" +
      "            text-align: center" +
      "        }\n" +
      "        th, td {\n" +
      "            padding: 0.25rem;\n" +
      "            text-align: left;\n" +
      "            border: 1px solid #ccc;\n" +
      "        }\n" +
      "        tbody tr:nth-child(odd) {\n" +
      "            background: #eee;\n" +
      "        }\n" +
      "    </style>" +
      "</HEAD>\n" +
      "\n" +
      "<HR>\n" +
      "<H1>"+pdfFileName.substring(0, pdfFileName.indexOf(".pdf"))+"</H1>"+
      "<table>");
  }

  private void terminateBookmarkHTMLDocument(PrintWriter bookmarkWriter) {
    bookmarkWriter.print("</table>\n<HR>\n" +
      "</BODY>\n" +
      "</HTML>");
  }

  private void writePageList(PrintWriter bookmarkWriter) {
    for (int pageNumber = 1; pageNumber <=numberOfPages; pageNumber++) {

      bookmarkWriter.print("<tr><td>page : " + pageNumber + " </td>");
      bookmarkWriter.print("<td><a href=\"PDF\\" + pageNumber + ".pdf\">PDF</a>&ensp;");
      bookmarkWriter.print("<a href=\"IMG\\" + pageNumber + "." + IMAGE_FORMAT + "\">IMG</a>&ensp;");

    }
  }

  public void writeBookmark(PDOutlineNode bookmark, String indentation, PDDocument document, PrintWriter bookmarkWriter) throws IOException {
    PDOutlineItem current = bookmark.getFirstChild();
    while (current != null) {
      PDPage currentPage = current.findDestinationPage(document);
      Integer pageNumber = document.getDocumentCatalog().getPages().indexOf(currentPage) + 1;

      bookmarkWriter.print("<tr><td>" + indentation + current.getTitle() + ": ");
      bookmarkWriter.print(pageNumber + " </td>");
      bookmarkWriter.print("<td><a href=\"PDF\\" + pageNumber + ".pdf\">PDF</a>&ensp;");
      bookmarkWriter.print("<a href=\"IMG\\" + pageNumber + "." + IMAGE_FORMAT + "\">IMG</a>&ensp;");

      writeBookmark(current, indentation + "&ensp;&ensp;&ensp;", document, bookmarkWriter);
      current = current.getNextSibling();
    }
  }


  public void initConstants() {
    try (PDDocument doc = PDDocument.load(file)) {

      PDFTextStripper pdfTextStripper = new PDFTextStripper();
      pdfTextStripper.setEndPage(3);
      this.isScanned = pdfTextStripper.getText(doc).trim().length() == 0;
      this.numberOfPages = doc.getNumberOfPages();
      int toNotify = (int) (numberOfPages * (double) PERCENT_OF_CONVERTED_PAGES_TO_NOTIFY_OBSERVERS / 100);
      this.numberOfPagesToNotifyObervers = toNotify>0?toNotify:1;



    } catch (IOException e) {
      LogErrorAndNotifyObservers(e);}
  }

  public boolean isScanned() {return isScanned;}

  public int getNumberOfPages() {return numberOfPages;}

  public String getPdfFileName() { return pdfFileName; }

  public String getResultFolderIMG() {return resultFolderIMG;}

  public String getResultFolderPDF() { return resultFolderPDF; }

  public String getResultFolder() {
    return resultFolder;
  }

  public Map<Integer, String> getTextPages() {return Collections.unmodifiableMap(textPages);}

  //
  private boolean isNotifiable(int pageNumber) {
    return (pageNumber%numberOfPagesToNotifyObervers==0)||(pageNumber==numberOfPages);
  }

  private void LogErrorAndNotifyObservers(Exception e) {
    LOGGER.log(Level.SEVERE, "Exception occur", e);
    this.errors.add(e.toString());
    notifyAllObservers();
  }

  @Override
  public void addObserver(Observer observer) {
    this.observers.add(observer);
  }

  @Override
  public void notifyAllObservers() {
    for (Observer<PDFConverter> observer : observers) {
      observer.update(this);
    }
  }

  public java.util.List<String> getErrors() {return errors;}

  public boolean isConverted() {return isConverted;}

  public int getTextProgress() {return textProgress;}

  public int getPagesProgress() {return pagesProgress;}

  public int getImagesProgress() {return imagesProgress;}
}
