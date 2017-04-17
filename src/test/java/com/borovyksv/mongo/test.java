package com.borovyksv.mongo;

import com.borovyksv.util.PDFConverter;
import com.borovyksv.util.PDFConverterFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class test {
    public static void main(String[] args) throws IOException {
//        PDDocument document = PDDocument.load(new File("D:\\pdf\\original\\.camel\\selection.pdf"));
//        PDDocumentOutline outline =  document.getDocumentCatalog().getDocumentOutline();
//        printBookmark(outline, "", document);
//        document.close();
        PDFConverter converter = PDFConverterFactory.newPDFConverter("D:\\pdf\\original\\.camel\\Новая папка\\Service Manual Acura.pdf");
        Instant start = Instant.now();

//        converter.savePagesPDFBOX();

        Instant end = Instant.now();
        System.out.println(Duration.between(start, end));


        converter.getTextPages().values().forEach(System.out::println);
    }
    public static void printBookmark(PDOutlineNode bookmark, String indentation, PDDocument document) throws IOException
    {
        PDOutlineItem current = bookmark.getFirstChild();



        while (current != null)
        {
            PDPage currentPage = current.findDestinationPage(document);
            Integer pageNumber = document.getDocumentCatalog().getPages().indexOf(currentPage) + 1;

            System.out.println(indentation + current.getTitle()+pageNumber);
            printBookmark(current, indentation + "    ", document);
            current = current.getNextSibling();
        }
    }
}
