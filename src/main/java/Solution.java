import java.io.IOException;

public class Solution {

    public static void main(String[] args) throws IOException {


        PDFManager manager = new PDFManager("D:\\pdf\\Acura.pdf");
        manager.PdfToText();
        manager.saveBookmarks();


    }

}
