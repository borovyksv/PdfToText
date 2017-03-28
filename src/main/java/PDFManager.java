import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.SimpleBookmark;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;
import org.apache.pdfbox.util.Splitter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PDFManager {
    private int IMAGE_DPI = 600;
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
    }

    public void PdfToText() {

        Tesseract tessInst = new Tesseract();
        tessInst.setDatapath(".");

        try (PDDocument document = PDDocument.loadNonSeq(file, null)) {

            //Instantiating Splitter class
            Splitter splitter = new Splitter();

            //splitting the pages of a PDF document
            List<PDDocument> Pages = splitter.split(document);

            //Creating an iterator
            Iterator<PDDocument> iterator = Pages.listIterator();

            //Saving each page as an individual document
            int pageNumber = 0;
            while (iterator.hasNext()) {
                ++pageNumber;
                PDDocument pd = iterator.next();

                //saving PDF
                pd.save(resultFolderPDF + pageNumber + ".pdf");
                System.out.println(pageNumber + ".pdf saved");

                //saving Image
                PDPage currentPage = (PDPage) pd.getDocumentCatalog().getAllPages().get(0);
                BufferedImage bim = currentPage.convertToImage(BufferedImage.TYPE_INT_RGB, IMAGE_DPI);
                ImageIOUtil.writeImage(bim, resultFolderIMG + pageNumber + ".png", IMAGE_DPI);
                System.out.println(pageNumber + ".png saved");

                //saving TXT
                try (PrintWriter out = new PrintWriter(resultFolderTXT + pageNumber + ".txt")) {
                    String result = tessInst.doOCR(bim);
                    String filteredResult = textFilter(result);
                    System.out.println(pageNumber + ".txt saved");
                    out.println(filteredResult);

                } catch (TesseractException e) {
                    System.err.println(e.getMessage());
                }

            }
        } catch (IOException | COSVisitorException e) {
            e.printStackTrace();
        }



    }

    private String textFilter(String result) {

        String pattern = "[^\\u0000-\\u007F\\u00b0\\n\\r\\t]|([^\\w\\d\\s]{2,}?)|(\\b.\\s.\\s.\\b)|\\s{2,}?|\\b\\D.?\\u0020..?\\u0020";
        return result.replaceAll(pattern, "");
    }

    public void saveBookmarks() {

        PdfReader reader = null;
        try (PrintWriter bookmarkWriter = new PrintWriter(resultFolder + "Bookmarks.html")) {
            reader = new PdfReader(pdfFilename);
            List<HashMap<String, Object>> list = SimpleBookmark.getBookmark(reader);

            //writing to HTML
            initializeBookmarkHTMLDocument(bookmarkWriter);
            printKids("", list, bookmarkWriter);
            terminateBookmarkHTMLDocument(bookmarkWriter);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //PdfReader is not AutoCloseable
            if (reader != null) reader.close();
        }
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
                        String page = entry.getValue().toString();
                        page = page.substring(0, page.length() - 4);
//                        System.out.println(page.substring(0, page.length() - 4));
                        bookmarkWriter.print(page + " </td>");
                        bookmarkWriter.print("<td><a href=\"PDF\\" + page + ".pdf\">PDF</a>&ensp;");
                        bookmarkWriter.print("<a href=\"IMG\\" + page + ".png\">IMG</a>&ensp;");
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