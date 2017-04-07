import util.PDFConverter;

import java.io.IOException;
import java.util.logging.Logger;

public class Solution {
    private static final Logger LOGGER = Logger.getLogger(Solution.class.getName());


    public static void main(String[] args) throws IOException, InterruptedException {
        PDFConverter converter = new PDFConverter("D:\\pdf\\Acura.pdf");
        converter.saveBookmarks();
        converter.savePagesAndImagesFromPdf();


    }

}
