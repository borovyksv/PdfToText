import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class PngToText
{
    public static void main(String[] args) {
        File image = new File("D:\\pdf\\selection.pdf-1.png");
        Tesseract tessInst = new Tesseract();
        tessInst.setDatapath(".");
        try {
            String result= tessInst.doOCR(image);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
}
