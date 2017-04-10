package com.borovyksv.util;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.SimpleBookmark;
import com.itextpdf.text.pdf.util.SmartPdfSplitter;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFConverter {
    private static final Logger LOGGER = Logger.getLogger(PDFConverter.class.getName());

    private int IMAGE_DPI = 300;
    private float IMAGE_COMPRESSION = 0.7f;
    private String IMAGE_FORMAT = "jpg";
    private int DIMENSIONS_DIVIDER = IMAGE_DPI / 100;


    private String pdfFilename;

    private File file;
    private String resultFolder;
    private String resultFolderPDF;
    private String resultFolderIMG;
    private Map<Integer, String> textPages = new HashMap<>();
//    private String resultFolderTXT;

    public PDFConverter(String fileDirectory) {
        this.pdfFilename = fileDirectory;
        this.file = new File(fileDirectory);
        this.resultFolder = file.getParent() + File.separator + file.getName().substring(0, file.getName().length() - 4) + "_parsed" + File.separator;
        this.resultFolderPDF = resultFolder + "PDF" + File.separator;
        this.resultFolderIMG = resultFolder + "IMG" + File.separator;
//        this.resultFolderTXT = resultFolder + "TXT" + File.separator;
        new File(resultFolder).mkdir();
        new File(resultFolderPDF).mkdir();
        new File(resultFolderIMG).mkdir();
//        new File(resultFolderTXT).mkdir();
        LOGGER.log(Level.INFO, String.format("PDFmanager for %s file initialized", fileDirectory));
    }

    static private String textFilter(String input) {
        String group = "[=;,_\\-/\\.\\\\\\\"\\'@~]";
        String pattern = String.format("(\\D)\\1{2,}?|[^\\u0000-\\u007F\\u00b0\\n\\r\\tВ]|\\s{3,}?|%1$s{3,}|%1$s+ %1$s+|( .{1,2} .{1,2} )+", group);
        //delete garbage from whole text
        input = input.replaceAll(pattern, "");
        //Filter short lines from garbage
        StringBuilder sb = new StringBuilder();
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

    public String getResultFolder() {
        return resultFolder;
    }

    public void convert() {
        saveBookmarks();
        savePagesAndImagesAndTextFromPdf();
    }

    public void savePagesFromPdf() {
        PdfReader reader = null;
        try {
            reader = new PdfReader(pdfFilename);
            SmartPdfSplitter splitter = new SmartPdfSplitter(reader);
            int pageNumber = 1;
            while (splitter.hasMorePages()) {
                splitter.split(new FileOutputStream(resultFolderPDF + pageNumber + ".pdf"), 200000);
                LOGGER.log(Level.INFO, String.format("%d.pdf saved", pageNumber));

                pageNumber++;
            }
        } catch (IOException | DocumentException e) {
            LOGGER.log(Level.SEVERE, "Exception occur", e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public void savePagesAndImagesAndTextFromPdf() {
        savePagesFromPdf();
        saveImagesAndTextFromPdf();
    }

    public void saveImagesAndTextFromPdf() {

        int docPagesSize = 0;
        try (PDDocument document = PDDocument.load(file)) {
            docPagesSize = document.getDocumentCatalog().getAllPages().size();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception occur", e);
        }

        for (int startPage = 1; startPage <= docPagesSize; startPage += 100) {

            // document must be reloaded every 100 pages to prevent memory leaks
            try (PDDocument document = PDDocument.load(file)) {
                int endPage = (startPage + 99) < docPagesSize ? startPage + 99 : docPagesSize;

                AtomicInteger counter = new AtomicInteger(startPage);

                ExecutorService executorService = Executors.newFixedThreadPool(4);

                for (int i = startPage; i <= endPage; i++) {
                    executorService.execute(() -> {
                        try {

                            int currentPage = counter.getAndIncrement();

                            BufferedImage image = getImage(document, currentPage);

                            saveTextToCollection(currentPage, image);

                            saveImage(currentPage, image);

                            image.flush();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
                executorService.shutdown();
                executorService.awaitTermination(30, TimeUnit.MINUTES);
                System.gc();

            } catch (IOException | InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Exception occur", e);
            }
        }
    }

//    private void saveText(int pageNumber, BufferedImage bufferedImage) {
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

    private void saveTextToCollection(int pageNumber, BufferedImage bufferedImage) {
        Tesseract tessInst = new Tesseract();

        try {
            String result = tessInst.doOCR(bufferedImage);
            String filteredResult = textFilter(result);

            textPages.put(pageNumber, filteredResult);

            LOGGER.log(Level.INFO, String.format("%d.txt saved to Collection", pageNumber));
        } catch (TesseractException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage getImage(PDDocument document, int page) throws IOException {

        PDPage currentPage = (PDPage) document.getDocumentCatalog().getAllPages().get(page - 1);
        return currentPage.convertToImage(BufferedImage.TYPE_INT_RGB, IMAGE_DPI);

    }

    private void saveImage(int pageNumber, BufferedImage bim) throws IOException {
        File file = new File(resultFolderIMG + pageNumber + "." + IMAGE_FORMAT);
        FileOutputStream output = new FileOutputStream(file);

        Image tmp = bim.getScaledInstance(bim.getWidth() / DIMENSIONS_DIVIDER, bim.getHeight() / DIMENSIONS_DIVIDER, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(bim.getWidth() / DIMENSIONS_DIVIDER, bim.getHeight() / DIMENSIONS_DIVIDER, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);


        ImageIOUtil.writeImage(dimg, IMAGE_FORMAT, output, IMAGE_DPI, IMAGE_COMPRESSION);
        g2d.dispose();
        LOGGER.log(Level.INFO, String.format("%d%s saved", pageNumber, "." + IMAGE_FORMAT));

    }

    public Map<Integer, String> getTextPages() {
        return Collections.unmodifiableMap(textPages);
    }


    public boolean saveBookmarks() {

        PdfReader reader = null;
        try {
            reader = new PdfReader(pdfFilename);
            java.util.List<HashMap<String, Object>> list = SimpleBookmark.getBookmark(reader);
            if (list == null) {
                LOGGER.log(Level.INFO, "Bookmarks list is empty");
                return false;
            } else {
                try (PrintWriter bookmarkWriter = new PrintWriter(resultFolder + "Bookmarks.html")) {
                    LOGGER.log(Level.INFO, String.format("%sBookmarks.html created", resultFolder));


                    //writing to HTML
                    initializeBookmarkHTMLDocument(bookmarkWriter);

                    LOGGER.log(Level.INFO, "printing bookmarks");
                    printKids("", list, bookmarkWriter);

                    terminateBookmarkHTMLDocument(bookmarkWriter);


                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Exception occur", e);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occur", e);
        } finally {
            //PdfReader is not AutoCloseable
            if (reader != null) reader.close();
        }
        LOGGER.log(Level.INFO, "Bookmarks saved");
        return true;
    }


    private void initializeBookmarkHTMLDocument(PrintWriter bookmarkWriter) {
        bookmarkWriter.print("<HTML>\n" +
                "<HEAD>\n" +
                "<TITLE>Bookmarks</TITLE>\n" +
                "<style>\n" +
                "        table {\n" +
                "            border-collapse: collapse;\n" +
                "            margin-left:auto; \n" +
                "            margin-right:auto;" +
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
                        bookmarkWriter.print("<tr><td>" + indentation + entry.getValue() + ": ");
                        break;
                    case "Page":
                        String page = entry.getValue().toString().split(" ")[0];
                        bookmarkWriter.print(page + " </td>");
                        bookmarkWriter.print("<td><a href=\"PDF\\" + page + ".pdf\">PDF</a>&ensp;");
                        bookmarkWriter.print("<a href=\"IMG\\" + page + "." + IMAGE_FORMAT + "\">IMG</a>&ensp;");
//                        bookmarkWriter.print("<a href=\"TXT\\" + page + ".txt\">TXT</a><br></td></tr>");
                        break;
                    case "Kids":
                        printKids("&ensp;&ensp;&ensp;" + indentation, (java.util.List<HashMap<String, Object>>) entry.getValue(), bookmarkWriter);
                        break;
                }
            });
        });
    }


}
