package com.borovyksv.mongo;

import org.apache.log4j.BasicConfigurator;
import org.ghost4j.document.DocumentException;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.RendererException;
import org.ghost4j.renderer.SimpleRenderer;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class Ghost4j {
    public static void main(String[] args) throws IOException, RendererException, DocumentException {


        BasicConfigurator.configure();

        PDFDocument document = new PDFDocument();
        document.load(new File("D:\\pdf\\original\\.camel\\2k14acadia.pdf"));
        int pageCount = document.getPageCount();


        Instant start = Instant.now();
        SimpleRenderer renderer = new SimpleRenderer();

        renderer.setResolution(300);


            renderer.setMaxProcessCount(0);

            renderer.render(document);


//        java.util.List<Image> render = renderer.render(document);
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end));

    }
}
