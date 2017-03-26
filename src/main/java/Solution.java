import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Solution {
    static int IMAGE_DPI = 150;
    public static void main(String[] args) throws IOException {
//        PDDocument document = PDDocument.load(new File("D:\\Acura.pdf"));
//        PDDocumentOutline outline =  document.getDocumentCatalog().getDocumentOutline();
//        printBookmark(outline, "");
//
//        PDFTextStripper pdfStripper = new PDFTextStripper();


//        document.close();

        String pdfFilename = "D:\\pdf\\selection.pdf";
        PDDocument document = PDDocument.loadNonSeq(new File(pdfFilename), null);
        List<PDPage> pdPages = document.getDocumentCatalog().getAllPages();
        int page = 0;
        for (PDPage pdPage : pdPages)
        {
            ++page;
            BufferedImage bim = pdPage.convertToImage(BufferedImage.TYPE_INT_RGB, IMAGE_DPI);
//            ImageIOUtil.writeImage(bim, pdfFilename + "-" + page + ".png", IMAGE_DPI);

            Tesseract tessInst = new Tesseract();
            tessInst.setDatapath(".");
            try {
                String result= tessInst.doOCR(bim);
                System.out.println(result);
            } catch (TesseractException e) {
                System.err.println(e.getMessage());
            }


            break;
        }

        document.close();

    }

//    private static void printBookmark(PDOutlineNode bookmark, String indentation) throws IOException
//    {
//        PDOutlineItem current = bookmark.getFirstChild();
//        while (current != null)
//        {
//            System.out.println(indentation + current.getTitle());
//            printBookmark(current, indentation + "    ");
//            current = current.getNextSibling();
//        }
//    }
}
