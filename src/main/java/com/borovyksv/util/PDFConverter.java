package com.borovyksv.util;

import com.borovyksv.mongo.observer.Observable;
import com.borovyksv.mongo.observer.Observer;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class PDFConverter implements Observable{
  private static final Logger LOGGER = Logger.getLogger(PDFConverter.class.getName());
  private static final int N_THREADS = 8;
  private static final int MESSAGES_TO_LOG = 10;
  private static final int PERCENT_OF_CONVERTED_PAGES_TO_NOTIFY_OBSERVERS = 5;


  private java.util.List<Observer<PDFConverter>> observers = new ArrayList<>();
  private java.util.List<String> errors = new ArrayList<>();

  private int pagesProgress;
  private int imagesProgress;
  private int textProgress;

  private String IMAGE_FORMAT = "png";
  private boolean isScanned = true;
  private boolean isConverted;


  private int numberOfPages = 0;
  private int numberOfPagesToNotifyObservers = 0;



  private String pdfFilePath;
  private String pdfFileName;
  private File file;
  private String resultFolder;
  private String resultFolderPDF;
  private String resultFolderIMG;
  private Map<Integer, String> textPages = new HashMap<>();
  private Map<String, Integer> bookmarkPages = new HashMap<>();

  public Map<String, Integer> getBookmarkPages() {
    return bookmarkPages;
  }

  /**
   *  While converter initializing, constructor creates folders to store converted files
   *  in the same destination where input file exists
   * */
  protected PDFConverter(String absolutePath) {
    this.pdfFilePath = absolutePath;
    this.file = new File(absolutePath);
    this.pdfFileName = this.file.getName();
    this.resultFolder = file.getParent() + File.separator + file.getName().replace(".pdf","") + File.separator;
    this.resultFolderPDF = resultFolder + "PDF" + File.separator;
    this.resultFolderIMG = resultFolder + "PNG" + File.separator;
    new File(resultFolder).mkdir();
    new File(resultFolderPDF).mkdir();
    new File(resultFolderIMG).mkdir();
    initConstants();

    LOGGER.log(Level.INFO, String.format("PDFConverter for %s file initialized", absolutePath));
  }

  protected PDFConverter(String absolutePath, String id, String originalFileName, String endFolder) {
    this.pdfFilePath = absolutePath;
    this.file = new File(absolutePath);
    this.pdfFileName = originalFileName;
    this.resultFolder = endFolder + id + File.separator;
    this.resultFolderPDF = resultFolder + "PDF" + File.separator;
    this.resultFolderIMG = resultFolder + "PNG" + File.separator;
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
      saveImagesAndText(true);
    } else {
      saveImages();
      saveText();
    }
  }


  public void savePages() {
    PdfReader reader = null;
    try {
      reader = new PdfReader(pdfFilePath);
      int numberOfPages = reader.getNumberOfPages();
      int pageNumber = 1;
      while (pageNumber<=numberOfPages) {
        String outFile = resultFolderPDF + pageNumber + ".pdf";

        Document document = new Document(reader.getPageSizeWithRotation(1));
        PdfCopy writer = new PdfCopy(document, new FileOutputStream(outFile));
        document.open();

        PdfImportedPage page = writer.getImportedPage(reader, pageNumber);
        writer.addPage(page);

        document.close();
        writer.close();

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
      Splitter splitter;
      double numberOfPages = doc.getNumberOfPages();


      for (int i = 1; i <= numberOfPages; i+=1000) {
        splitter=new Splitter();
        splitter.setStartPage(i);
        splitter.setEndPage((i+999)>=numberOfPages?(int)numberOfPages:(i+999));

        java.util.List<PDDocument> pages = splitter.split(doc);
        int counter = i;


        for (PDDocument page : pages) {
          try {
            int pageNumber = counter++;

            String extract = new PDFTextStripper().getText(page);

            String result = extract.replaceAll("[\r\n]|( \\.){3,}", " ");
            textPages.put(pageNumber, result);

            // percent(%) of converted text pages
            textProgress = getProgressValue(this.numberOfPages, pageNumber, textProgress);

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
      }

      System.out.println("number of pages "+numberOfPages);
      System.out.println("size of text pages "+textPages.size());
    } catch (IOException e) {
      LogErrorAndNotifyObservers(e);}
  }



  public void saveImages() {
      saveImagesAndText(false);
  }


  private int getProgressValue(int numberOfPages, int pageNumber, int currentValue) {
    int result = (100 * pageNumber / numberOfPages);
    result = result <= currentValue ? currentValue : result;
    return result;
  }

  /**
   * This method combines two another methods (@saveTextFromPage() and @saveImage()) within,
   * to optimize resource consuming (BufferedImage.class) needed for execution
   * */
  private void saveImagesAndText(boolean saveText) {


    try(RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel()) {

      ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

      PDFFile pdfFile = new PDFFile(buf);


      ArrayList<Thread> threads = new ArrayList<>(N_THREADS);

      IntStream.range(0, N_THREADS).parallel().forEach((i) -> threads.add(
        new ConvertThread(this, pdfFile, saveText)
      ));

      ForkJoinPool forkJoinPool = new ForkJoinPool(N_THREADS);

      try {
        forkJoinPool.submit(() -> threads.stream().parallel().forEach((i) -> i.start())).get();
        forkJoinPool.submit(() -> threads.stream().parallel().forEach((i) ->
        {
          try {
            i.join();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        })).get();
      } catch (ExecutionException e) {
        LOGGER.log(Level.INFO, e.getMessage());
      }

      threads.clear();

    } catch (IOException | InterruptedException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
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

  protected void saveTextFromPage(int pageNumber, BufferedImage bufferedImage) {
    Tesseract tessInst = new Tesseract();
    tessInst.setPageSegMode(1);

    try {
      String result = tessInst.doOCR(bufferedImage);
      String filteredResult = textFilter(result);

      textPages.put(pageNumber, filteredResult);
      System.out.println(filteredResult);

      // percent(%) of converted text pages
      textProgress = getProgressValue(numberOfPages, pageNumber, textProgress);

      if (isNotifiable(pageNumber)) notifyAllObservers();

      if (pageNumber % MESSAGES_TO_LOG == 0) LOGGER.log(Level.INFO, String.format("%d.txt saved", pageNumber));
    } catch (TesseractException e) {
      LogErrorAndNotifyObservers(e);
    }

  }


  protected BufferedImage getImage(PDFFile pdffile, int pageNumber) throws IOException {
        int scale = 3;

        PDFPage page = pdffile.getPage(pageNumber);
        int width = (int) page.getBBox().getWidth();
        int height = (int) page.getBBox().getHeight();

        Rectangle rect = new Rectangle(0, 0, width, height);
        int rotation = page.getRotation();
        Rectangle rectl = rect;

        if (rotation == 90 || rotation == 270) {
          rectl = new Rectangle(0, 0, rect.height, rect.width);
        }


        Image pdfImage = page.getImage(rect.width*scale, rect.height*scale, rectl, null, true, true);
        BufferedImage bufferedImage = new BufferedImage(rect.width*scale, rect.height*scale, BufferedImage.TYPE_BYTE_BINARY);
        bufferedImage.createGraphics().drawImage(pdfImage, 0, 0, null);

        return bufferedImage;
  }

  protected void saveImage(int pageNumber, BufferedImage bim) throws IOException {
    File file = new File(resultFolderIMG + pageNumber + "." + IMAGE_FORMAT);
    ImageIO.write(bim, IMAGE_FORMAT, file);

    // percent(%) of converted images
    imagesProgress = getProgressValue(numberOfPages, pageNumber, imagesProgress);
    if (isNotifiable(pageNumber)) notifyAllObservers();

    if (pageNumber % MESSAGES_TO_LOG == 0) LOGGER.log(Level.INFO, String.format("%d%s saved", pageNumber, "." + IMAGE_FORMAT));

  }

  private String textFilter(String input) {
    String pattern = "[/.\\-—_\"']{3,}|[uJLlI\\\\/)(\\-_, ]{5,}";
    return input.replaceAll("\n", " ").replaceAll(" {3,}", "").replaceAll(pattern, "");
  }

  //converts fileName to indexable string
//  private String getTextMark() {
//    return pdfFileName.substring(0, pdfFileName.indexOf(".pdf")).replaceAll("_", " ") + "\n";
//  }

  public boolean saveBookmarksToHTML() {
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
          writeBookmarkToHTML(outline, "", document, bookmarkWriter);

        }

        terminateBookmarkHTMLDocument(bookmarkWriter);

      } catch (IOException e) {
        LogErrorAndNotifyObservers(e);}
    } catch (Exception e) {
      LogErrorAndNotifyObservers(e);}

    LOGGER.log(Level.INFO, "Bookmarks saved");
    return true;
  }

  public boolean saveBookmarks() {
    try (PDDocument document = PDDocument.load(file)) {
      PDDocumentOutline outline = document.getDocumentCatalog().getDocumentOutline();

        LOGGER.log(Level.INFO, String.format("%sBookmarks.html created", resultFolder));

        if (outline == null) {

          LOGGER.log(Level.INFO, "PDF file does not have Bookmarks");
          return false;

        } else {
          LOGGER.log(Level.INFO, "writing bookmarks");
          writeBookmark(outline, "", document);

        }


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

      bookmarkWriter.print("<tr><td>pageNumber : " + pageNumber + " </td>");
      bookmarkWriter.print("<td><a href=\"PDF\\" + pageNumber + ".pdf\">PDF</a>&ensp;");
      bookmarkWriter.print("<a href=\"IMG\\" + pageNumber + "." + IMAGE_FORMAT + "\">IMG</a>&ensp;");

    }
  }

  public void writeBookmarkToHTML(PDOutlineNode bookmark, String indentation, PDDocument document, PrintWriter bookmarkWriter) throws IOException {
    PDOutlineItem current = bookmark.getFirstChild();
    while (current != null) {
      PDPage currentPage = current.findDestinationPage(document);
      Integer pageNumber = document.getDocumentCatalog().getPages().indexOf(currentPage) + 1;

      bookmarkWriter.print("<tr><td>" + indentation + current.getTitle() + ": ");
      bookmarkWriter.print(pageNumber + " </td>");
      bookmarkWriter.print("<td><a href=\"PDF\\" + pageNumber + ".pdf\">PDF</a>&ensp;");
      bookmarkWriter.print("<a href=\"IMG\\" + pageNumber + "." + IMAGE_FORMAT + "\">IMG</a>&ensp;");

      bookmarkPages.put(indentation+current.getTitle(), pageNumber);

      writeBookmarkToHTML(current, indentation + "- ", document, bookmarkWriter);
      current = current.getNextSibling();
    }
  }

  public void writeBookmark(PDOutlineNode bookmark, String indentation, PDDocument document) throws IOException {
    PDOutlineItem current = bookmark.getFirstChild();
    while (current != null) {
      PDPage currentPage = current.findDestinationPage(document);
      Integer pageNumber = document.getDocumentCatalog().getPages().indexOf(currentPage) + 1;

      bookmarkPages.put(indentation+current.getTitle(), pageNumber);

      writeBookmark(current, indentation + "- ", document);
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
      this.numberOfPagesToNotifyObservers = toNotify>0?toNotify:1;



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
    return (pageNumber% numberOfPagesToNotifyObservers ==0)||(pageNumber==numberOfPages);
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
