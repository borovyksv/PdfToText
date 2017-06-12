package com.borovyksv.util;

import com.sun.pdfview.PDFFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

class ConvertThread extends Thread {
    private static final Logger LOGGER = Logger.getLogger(ConvertThread.class.getName());
    private static volatile int currentPage;
    private final PDFConverter converter;
    private final PDFFile pdfFile;
    private boolean saveText;
    public ConvertThread(PDFConverter converter, PDFFile pdfFile, boolean saveText) {
        this.converter = converter;
        this.pdfFile = pdfFile;
        this.saveText = saveText;
    }

    public static int getCurrentPage() {
        return currentPage;
    }

    public static void setCurrentPage(int currentPage) {
        ConvertThread.currentPage = currentPage;
    }

    @Override
    public void run() {
        int numberOfPages = pdfFile.getNumPages();
        ArrayList<Integer> pageNumbersOfUnhandledImages = new ArrayList<>();

        while (incrementAndGetCurrentPage() <= numberOfPages) {

            convertPage(currentPage, pageNumbersOfUnhandledImages);

        }
        if (pageNumbersOfUnhandledImages.size() > 0) {
            for (Integer unhandledPageNumber : pageNumbersOfUnhandledImages) {
                System.out.println("pageNumbersOfUnhandledImages called for page: #"+unhandledPageNumber);
                convertPage(unhandledPageNumber, pageNumbersOfUnhandledImages);

            }
        }
        Thread.currentThread().interrupt();
    }

    private void convertPage(int page, ArrayList<Integer> handler) {
        try {
            BufferedImage bim = converter.getImage(pdfFile, page);

            converter.saveImage(page, bim);
            if (saveText) converter.saveTextFromPage(page, bim);

        } catch (OutOfMemoryError oome) {
            LOGGER.log(Level.INFO, String.format("\n\n\n\n\n\n\n%d image caused OutOfMemory, retrying to convert in another tread", page));
            handler.add(page);
            run();
        } catch (IOException e) {
            LOGGER.log(Level.INFO, e.getMessage());
            converter.getErrors().add(e.getMessage());
        }

    }

    private synchronized int incrementAndGetCurrentPage() {
        return ++currentPage;
    }
}
