//package com.borovyksv.util;
//
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.text.PDFTextStripper;
//
//import java.io.File;
//import java.io.IOException;
//
//public class PDFReader {
//    public static void main(String args[]) throws IOException {
//
////        PDDocument doc = PDDocument.load(new File("D:\\pdf\\original\\.camel\\Acura.pdf"));
////        Splitter splitter = new Splitter();
////        splitter.setStartPage(1);
////        splitter.setEndPage(3);
////        List<PDDocument> split = splitter.split(doc);
////
////        for (PDDocument document: split){
////            System.out.println(document.toString());
//////        System.out.println(new PDFTextStripper().getText(doc).trim().length());
////        System.out.println(new PDFTextStripper().getText(document));
////        }
//
////        PDDocument doc = PDDocument.load(new File("D:\\pdf\\original\\.camel\\Acura.pdf"));
////        PDFTextStripper pdfTextStripper = new PDFTextStripper();
////        pdfTextStripper.setEndPage(3);
////        System.out.println(pdfTextStripper.getText(doc).trim().length());
//        System.out.println(isScannedPDF(new File("D:\\pdf\\original\\.camel\\2k14acadia.pdf")));
//
//
//
//
////        PDFTextStripper pdfStripper = null;
////        PDDocument pdDoc = null;
////        COSDocument cosDoc = null;
////        File file = new File("D:\\pdf\\original\\.camel\\Acura.pdf");
////        try {
////            PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
////            parser.parse();
////            cosDoc = parser.getDocument();
////            pdfStripper = new PDFTextStripper();
////            pdDoc = new PDDocument(cosDoc);
////            pdfStripper.setStartPage(1);
////            pdfStripper.setEndPage(5);
////            String parsedText = pdfStripper.getText(pdDoc).trim();
////            System.out.println("start");
////            System.out.println(parsedText);
////            System.out.println(parsedText.length());
////            System.out.println("end");
////        } catch (IOException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
//    }
//
//    private static boolean isScannedPDF(File file){
//        try(PDDocument doc = PDDocument.load(file)) {
//            PDFTextStripper pdfTextStripper = new PDFTextStripper();
//            pdfTextStripper.setEndPage(3);
//            return pdfTextStripper.getText(doc).trim().length()==0;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return true;
//    }
//}
