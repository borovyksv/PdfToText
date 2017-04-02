import util.PDFConverter;

import java.io.IOException;

public class Solution {

    public static void main(String[] args) throws IOException {
        PDFConverter converter = new PDFConverter("D:\\pdf\\Acura.pdf");
//        manager.saveBookmarks();
        converter.savePagesAndImagesFromPdf();
//        converter.saveImagesFromPdf();
//        PDFManager manager = new PDFManager("D:\\pdf\\Acura.pdf");
//        manager.convertPDF();
    }

}
