package com.borovyksv;

import com.borovyksv.util.PDFConverter;
import com.borovyksv.util.PDFConverterFactory;

public class testModes {
  private static final double DIMENSIONS_DIVIDER = 1.5;
  public static final int width = 3000;

  public static void main(String[] args) {

//    String[] stirng = run().split("\n");
//    for (String sub : stirng) {
//      System.out.println(filter(sub));
//    }



//  int result = (int)(width / DIMENSIONS_DIVIDER);
//    System.out.println(result);



//    PDFConverter converter = PDFConverterFactory.newPDFConverter("D:\\pdf\\original\\2015 Toyota Tundra Factory Service Repair Manual with Bookmarks.pdf");
    PDFConverter converter = PDFConverterFactory.newPDFConverter("D:\\pdf\\original\\5937a20bb09013278cec593b.pdf");
    converter.convert();


//    File[] files = PdfUtilities.convertPdf2Png(new File("D:\\pdf\\original\\.camel\\selection.pdf"));
//    Instant start = Instant.now();
//
//    for (int j = 0; j < files.length; j++) {
//      for (int i = 0; i < 14; i++) {
//        Tesseract tess = new Tesseract();
//        tess.setPageSegMode(i);
//
//
//        try {
//          Instant start1 = Instant.now();
//          System.out.println("Page:"+j+" Mode:"+i+" output: "+tess.doOCR(files[j]).replaceAll("\n", ""));
//          Instant end1 = Instant.now();
//          System.out.println("Time for page :"+ Duration.between(start1, end1));
//          System.out.println("================================================================================================================================================================");
//        } catch (TesseractException e) {
//          System.err.println("Page:"+j+" Mode:"+i);
//          e.printStackTrace();
//        }
//      }
//      System.out.println("================================================================================================================================================================");
//      System.out.println("================================================================================================================================================================");
//      System.out.println("================================================================================================================================================================");
//
//    }
//    Instant end = Instant.now();
//    System.out.println("Duration :"+ Duration.between(start, end));




  }

  private static String filter(String sub) {
    String pattern = "[/.\\-—_\"']{3,}|[uJLlI\\\\/)(\\-_, ]{5,}";
    return sub.replaceAll(pattern, "");
  }

  public static String run(){
    return "";
  }

}
