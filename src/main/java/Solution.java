import java.io.IOException;

public class Solution {
    static int IMAGE_DPI = 150;

    public static void main(String[] args) throws IOException {
//        PDDocument document = PDDocument.load(new File("D:\\Acura.pdf"));
//        PDDocumentOutline outline =  document.getDocumentCatalog().getDocumentOutline();
//        printBookmark(outline, "");
//
//        PDFTextStripper pdfStripper = new PDFTextStripper();


//        document.close();

        PDFManager manager = new PDFManager("D:\\pdf\\Acura.pdf");
        manager.toText();


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
