package util;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.SimpleBookmark;
import com.itextpdf.text.pdf.util.SmartPdfSplitter;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;
import org.apache.pdfbox.util.Splitter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class PDFConverter {
    private static final Logger LOGGER = Logger.getLogger(PDFConverter.class.getName());

    private int IMAGE_DPI = 300;
    private float IMAGE_COMPRESSION = 0.7f;
    private String IMAGE_FORMAT = "jpg";


    private String pdfFilename;
//    private Integer startPage;
//    private Integer endPage;

    private File file;
    private String resultFolder;
    private String resultFolderPDF;
    private String resultFolderIMG;
    private String resultFolderTXT;


    public PDFConverter(String pdfFilename) {
        this.pdfFilename = pdfFilename;
        this.file = new File(pdfFilename);
        this.resultFolder = file.getParent() + File.separator + file.getName().substring(0, file.getName().length() - 4) + "_parsed" + File.separator;
        this.resultFolderPDF = resultFolder + "PDF" + File.separator;
        this.resultFolderIMG = resultFolder + "IMG" + File.separator;
        this.resultFolderTXT = resultFolder + "TXT" + File.separator;
        new File(resultFolder).mkdir();
        new File(resultFolderPDF).mkdir();
        new File(resultFolderIMG).mkdir();
        new File(resultFolderTXT).mkdir();
        LOGGER.log(Level.INFO, String.format("PDFmanager for %s file initialized", pdfFilename));
    }

    public void savePagesFromPdf() {
        PdfReader reader = null;
        try {
            reader = new PdfReader(pdfFilename);
            SmartPdfSplitter splitter = new SmartPdfSplitter(reader);
            int pageNumber = 1;
            while (splitter.hasMorePages()) {
                splitter.split(new FileOutputStream(resultFolderPDF + pageNumber + ".pdf"), 200000);
                LOGGER.log(Level.INFO, String.format("%d.pdf with ITEXT saved", pageNumber));

                pageNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public void savePagesAndImagesFromPdf() {

        //todo REPLACE
        savePagesFromPdf();



        int docPagesSize = 0;
        try (PDDocument document = PDDocument.load(file)) {
            docPagesSize = document.getDocumentCatalog().getAllPages().size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int startPage = 1; startPage < docPagesSize; startPage += 100) {
            //the document might be reloaded every 100 pages for prevent memory leaks
            try (PDDocument document = PDDocument.load(file)) {
                int endPage = (startPage + 99) < docPagesSize ? startPage + 99 : docPagesSize;

                AtomicInteger counter = new AtomicInteger(startPage - 1);

                Splitter splitter = new Splitter();
                splitter.setStartPage(startPage);
                splitter.setEndPage(endPage);


                List<PDDocument> pages = splitter.split(document);
                LOGGER.log(Level.INFO, "SPLITTING....................." + startPage + " - " + endPage);

                ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

                for (PDDocument page : pages) {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                int currentPage = counter.incrementAndGet();

                                BufferedImage image = getImage(page);

                                saveText(currentPage, image);

                                saveImage(currentPage, image);

                                image.flush();

                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {

                                if (page != null) try {
                                    page.close();
                                } catch (IOException e) {
                                    LOGGER.log(Level.SEVERE, "Exception occur", e);
                                }
                            }
                        }
                    });
                }
                executorService.shutdown();
                executorService.awaitTermination(30, TimeUnit.MINUTES);
//                pages.parallelStream().forEach(page -> {
//                    try {
//
//                        int currentPage = counter.incrementAndGet();
//
//                        savePage(page, currentPage);
//
//                        BufferedImage image = getImage(page);
//
//                        saveText(currentPage, image);
//
//                        saveImage(currentPage, image);
//
//                        image.flush();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//
//                        if (page != null) try {
//                            page.close();
//                        } catch (IOException e) {
//                            LOGGER.log(Level.SEVERE, "Exception occur", e);
//                        }
//                    }
//                });
                System.gc();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Exception occur", e);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void saveText(int pageNumber, BufferedImage bufferedImage) {
        Tesseract tessInst = new Tesseract();

        try (PrintWriter out = new PrintWriter(resultFolderTXT + pageNumber + ".txt")) {
            String result = tessInst.doOCR(bufferedImage);
//            LOGGER.log(Level.INFO, String.format("%d.txt converted", pageNumber));
            String filteredResult = textFilter(result);
//            LOGGER.log(Level.INFO, String.format("%d.txt filtered", pageNumber));
            out.println(filteredResult);
            LOGGER.log(Level.INFO, String.format("%d.txt saved", pageNumber));

        } catch (TesseractException | FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Exception occur", e);
        }
    }


    private String textFilter(String input) {
        String group = "[=;,_\\-/\\.\\\\\\\"\\'@~]";
        String pattern = String.format("(\\D)\\1{2,}?|[^\\u0000-\\u007F\\u00b0\\n\\r\\tВ]|\\s{3,}?|%1$s{3,}|%1$s+ %1$s+|( .{1,2} .{1,2} )+", group);
        //deleting garbage
        input = input.replaceAll(pattern, "");
        //filtering short lines
        input = Arrays.stream(input.split("\n"))
                .filter(s -> s.length() > 5)
                .collect(Collectors.joining("\n"));
        return input;
    }


    private BufferedImage getImage(PDDocument page) throws IOException {

        PDPage currentPage = (PDPage) page.getDocumentCatalog().getAllPages().get(0);
        return currentPage.convertToImage(BufferedImage.TYPE_INT_RGB, IMAGE_DPI);

    }

    private void saveImage(int pageNumber, BufferedImage bim) throws IOException {
        File file = new File(resultFolderIMG + pageNumber + "." + IMAGE_FORMAT);
        FileOutputStream output = new FileOutputStream(file);

        Image tmp = bim.getScaledInstance(720, 936, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(720, 936, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);


        ImageIOUtil.writeImage(dimg, IMAGE_FORMAT, output, IMAGE_DPI, IMAGE_COMPRESSION);
        g2d.dispose();
        LOGGER.log(Level.INFO, String.format("%d%s saved", pageNumber, "." + IMAGE_FORMAT));

    }

    private void savePage(PDDocument page, int pageNumber) throws IOException, COSVisitorException {
        // this if statement used to prevent PDFbox BUG with saving first page of document: result has a huge size
        if (pageNumber == 1) {
            PdfReader reader = null;
            try {
                reader = new PdfReader(pdfFilename);
                SmartPdfSplitter splitter = new SmartPdfSplitter(reader);
                splitter.split(new FileOutputStream(resultFolderPDF + pageNumber + ".pdf"), 200000);
                LOGGER.log(Level.INFO, String.format("%d.pdf saved", pageNumber));

            } catch (DocumentException e) {
                LOGGER.log(Level.SEVERE, "Exception occur", e);
            } catch (FileNotFoundException e) {
                LOGGER.log(Level.SEVERE, "Exception occur", e);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Exception occur", e);
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }

        } else {
            page.save(resultFolderPDF + pageNumber + ".pdf");
            LOGGER.log(Level.INFO, String.format("%d.pdf saved", pageNumber));
        }

    }

    public void saveBookmarks() {

        new Thread(() -> {
            PdfReader reader = null;
            try (PrintWriter bookmarkWriter = new PrintWriter(resultFolder + "Bookmarks.html")) {
                LOGGER.log(Level.INFO, String.format("%sBookmarks.html created", resultFolder));

                reader = new PdfReader(pdfFilename);
                java.util.List<HashMap<String, Object>> list = SimpleBookmark.getBookmark(reader);

                //writing to HTML
                initializeBookmarkHTMLDocument(bookmarkWriter);

                LOGGER.log(Level.INFO, "printing bookmarks");
                printKids("", list, bookmarkWriter);

                terminateBookmarkHTMLDocument(bookmarkWriter);


            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Exception occur", e);
            } finally {
                //PdfReader is not AutoCloseable
                if (reader != null) reader.close();
            }
            LOGGER.log(Level.INFO, "Bookmarks saved");
        }).start();
    }


    private void initializeBookmarkHTMLDocument(PrintWriter bookmarkWriter) {
        bookmarkWriter.print("<HTML>\n" +
                "<HEAD>\n" +
                "<TITLE>Bookmarks</TITLE>\n" +
                "<style>\n" +
                "        table {\n" +
                "            border-collapse: collapse;\n" +
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
                "<HR>\n<table>");
    }

    private void terminateBookmarkHTMLDocument(PrintWriter bookmarkWriter) {
        bookmarkWriter.print("</table>\n<HR>\n" +
                "</BODY>\n" +
                "</HTML>");
    }

    private void printKids(String indentation, java.util.List<HashMap<String, Object>> value, PrintWriter bookmarkWriter) {
        value.forEach(map -> {
            map.entrySet().forEach(entry -> {
                switch (entry.getKey()) {
                    case "Title":
//                        System.out.print(indentation + entry.getValue() + ": ");
                        bookmarkWriter.print("<tr><td>" + indentation + entry.getValue() + ": ");
                        break;
                    case "Page":
                        String page = entry.getValue().toString().split(" ")[0];
//                        page = page.substring(0, page.length() - 4);
//                        System.out.println(page.substring(0, page.length() - 4));
                        bookmarkWriter.print(page + " </td>");
                        bookmarkWriter.print("<td><a href=\"PDF\\" + page + ".pdf\">PDF</a>&ensp;");
                        bookmarkWriter.print("<a href=\"IMG\\" + page + "." + IMAGE_FORMAT + "\">IMG</a>&ensp;");
                        bookmarkWriter.print("<a href=\"TXT\\" + page + ".txt\">TXT</a><br></td></tr>");
                        break;
                    case "Kids":
                        printKids("&ensp;&ensp;&ensp;" + indentation, (java.util.List<HashMap<String, Object>>) entry.getValue(), bookmarkWriter);
                        break;
                }
            });
        });
    }


}
