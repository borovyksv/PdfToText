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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PDFManager {

    private static final Logger LOGGER = Logger.getLogger(PDFManager.class.getName());

    private final int IMAGE_DPI = 600;
    private final float IMAGE_COMPRESSION = 0.4f;
    private final String IMAGE_FORMAT = "jpg";


    private String pdfFilename;
    private File file;
    private String resultFolder;
    private String resultFolderPDF;
    private String resultFolderIMG;
    private String resultFolderTXT;


    public PDFManager(String pdfFilename) {
        this.pdfFilename = pdfFilename;
        this.file = new File(pdfFilename);
        this.resultFolder = file.getParent() + File.separator + file.getName().substring(0, file.getName().length() - 4) + "_parsed" + File.separator;
        this.resultFolderPDF = resultFolder + File.separator + "PDF" + File.separator;
        this.resultFolderIMG = resultFolder + File.separator + "IMG" + File.separator;
        this.resultFolderTXT = resultFolder + File.separator + "TXT" + File.separator;
        new File(resultFolder).mkdir();
        new File(resultFolderPDF).mkdir();
        new File(resultFolderIMG).mkdir();
        new File(resultFolderTXT).mkdir();
        LOGGER.log(Level.INFO, String.format("PDFmanager for %s file initialized", pdfFilename));
    }

    public void convertPDF() {


        //load document
        try (PDDocument document = PDDocument.load(file)) {
            LOGGER.log(Level.INFO, String.format("Document %s loaded", document));

            //Instantiating Splitter class
            Splitter splitter = new Splitter();
            //initializing Tesseract
            Tesseract tessInst = new Tesseract();
            tessInst.setDatapath(".");
            //initializing page counter
            AtomicInteger counter = new AtomicInteger(0);

            //splitting the pages of a PDF document
            List<PDDocument> pages = splitter.split(document);

            //Saving each page as an individual document
            pages.forEach(page -> {
                try {
                    int pageNumber = counter.incrementAndGet();

                    //saving PDF
                    savePage(page, pageNumber);
                    //getting Image
                    BufferedImage bufferedImage = getImage(page);
                    //saving Image
                    saveImage(pageNumber, bufferedImage);
                    //saving TXT
                    saveText(tessInst, pageNumber, bufferedImage);

                } catch (IOException | COSVisitorException e) {
                    LOGGER.log(Level.SEVERE, "Exception occur", e);
                }
            });

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception occur", e);
        }


    }

    private BufferedImage getImage(PDDocument page) throws IOException {

        PDPage currentPage = (PDPage) page.getDocumentCatalog().getAllPages().get(0);
        return currentPage.convertToImage(BufferedImage.TYPE_INT_RGB, IMAGE_DPI);
    }

    private void saveText(Tesseract tessInst, int pageNumber, BufferedImage bufferedImage) {
        try (PrintWriter out = new PrintWriter(resultFolderTXT + pageNumber + ".txt")) {
            String result = tessInst.doOCR(bufferedImage);
            LOGGER.log(Level.INFO, String.format("%d.txt converted", pageNumber));
            String filteredResult = textFilter(result);
            LOGGER.log(Level.INFO, String.format("%d.txt filtered", pageNumber));
            out.println(filteredResult);
            LOGGER.log(Level.INFO, String.format("%d.txt saved", pageNumber));

        } catch (TesseractException | FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Exception occur", e);
        }
    }

    private void saveImage(int pageNumber, BufferedImage bim) throws IOException {
        File file = new File(resultFolderIMG + pageNumber + "." + IMAGE_FORMAT);
        FileOutputStream output = new FileOutputStream(file);

        Image tmp = bim.getScaledInstance(600, 780, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(600, 780, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();


        ImageIOUtil.writeImage(dimg, IMAGE_FORMAT, output, IMAGE_DPI, IMAGE_COMPRESSION);
        LOGGER.log(Level.INFO, String.format("%d%s saved", pageNumber, "." + IMAGE_FORMAT));

    }

    private void savePage(PDDocument pd, int pageNumber) throws IOException, COSVisitorException {
        // this if statement used to prevent PDFbox BUG with saving first page of document: result has a huge size
        if (pageNumber == 1) {
            PdfReader reader=null;
            try {
                reader = new PdfReader(pdfFilename);
                SmartPdfSplitter splitter = new SmartPdfSplitter(reader);
                splitter.split(new FileOutputStream(resultFolderPDF + pageNumber + ".pdf"), 200000);
                LOGGER.log(Level.INFO, String.format("%d.pdf saved", pageNumber));

            } catch (DocumentException e) {
                LOGGER.log(Level.SEVERE, "Exception occur", e);
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }

        } else {
            pd.save(resultFolderPDF + pageNumber + ".pdf");
            LOGGER.log(Level.INFO, String.format("%d.pdf saved", pageNumber));
        }

    }

    private String textFilter(String input) {
        String group = "[=;,_\\-/\\.\\\\\\\"\\'@~]";
        String pattern = String.format("(\\D)\\1{2,}?|[^\\u0000-\\u007F\\u00b0\\n\\r\\tВ]|\\s{3,}?|%1$s{3,}|%1$s+ %1$s+|( .{1,2} .{1,2} )+", group);
        //deleting garbage
        input = input.replaceAll(pattern, "");
        //filtering short lines
        input = Arrays.stream(input.split("\n"))
                .filter(s -> s.length()>5)
                .collect(Collectors.joining("\n"));
        return input;
    }

    public void saveBookmarks() {

        new Thread(() -> {
            PdfReader reader = null;
            try (PrintWriter bookmarkWriter = new PrintWriter(resultFolder + "Bookmarks.html")) {
                LOGGER.log(Level.INFO, String.format("%sBookmarks.html created", resultFolder));

                reader = new PdfReader(pdfFilename);
                List<HashMap<String, Object>> list = SimpleBookmark.getBookmark(reader);

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

    private void printKids(String indentation, List<HashMap<String, Object>> value, PrintWriter bookmarkWriter) {
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
                        bookmarkWriter.print("<a href=\"IMG\\" + page + "." +IMAGE_FORMAT+"\">IMG</a>&ensp;");
                        bookmarkWriter.print("<a href=\"TXT\\" + page + ".txt\">TXT</a><br></td></tr>");
                        break;
                    case "Kids":
                        printKids("&ensp;&ensp;&ensp;" + indentation, (List<HashMap<String, Object>>) entry.getValue(), bookmarkWriter);
                        break;
                }
            });
        });
    }

}