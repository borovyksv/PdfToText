import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class PDFManager {
    private int IMAGE_DPI = 150;
    private File file;
    private String resultFolder;


    public PDFManager(String pdfFilename) {
        this.file = new File(pdfFilename);
        this.resultFolder = file.getParent() + File.separator + file.getName().substring(0, file.getName().length() - 4) + "_parsed" + File.separator;
        new File(resultFolder).mkdir();
    }

    public void PdfToText() {

        Tesseract tessInst = new Tesseract();
        tessInst.setDatapath(".");


        try (PDDocument document = PDDocument.loadNonSeq(file, null)) {
            List<PDPage> pdPages = document.getDocumentCatalog().getAllPages();


            int page = 0;
            for (PDPage pdPage : pdPages) {
                ++page;
                System.out.println("page: " + page + " loaded");
                BufferedImage bim = pdPage.convertToImage(BufferedImage.TYPE_INT_RGB, IMAGE_DPI);
                System.out.println("page: " + page + " converted to Image");


                try (PrintWriter out = new PrintWriter(resultFolder + "Page -" + page + ".txt")) {
                    String result = tessInst.doOCR(bim);
                    System.out.println("page: " + page + " converted to Text");
                    out.println(result);

                } catch (TesseractException e) {
                    System.err.println(e.getMessage());
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBookmarks() {
        try (PDDocument document = PDDocument.load(file);
             PrintWriter bookmarkWriter = new PrintWriter(resultFolder + "Bookmarks.txt")) {
            bookmarkWriter.println(file.getName()+" bookmarks:");
            System.out.println("Writing bookmarks to file: ");
            PDDocumentOutline outline = document.getDocumentCatalog().getDocumentOutline();
            printBookmark(bookmarkWriter, outline, "");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void printBookmark(PrintWriter bookmarkWriter, PDOutlineNode bookmark, String indentation) throws IOException {
        PDOutlineItem current = bookmark.getFirstChild();
        while (current != null) {
            bookmarkWriter.println(indentation + current.getTitle());
            System.out.println(indentation + current.getTitle());
            printBookmark(bookmarkWriter, current, indentation + "    ");
            current = current.getNextSibling();
        }
    }
}