package com.borovyksv.util;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PDFConverterFactory {
    private static final Logger LOGGER = Logger.getLogger(PDFConverterFactory.class.getName());


//    public static PDFConverter newPDFConverter(InputStream stream, String nameWithId) {
//        Path tmpFile=null;
//        try {
//
//            if (!nameWithId.endsWith(".pdf")) throw new IllegalArgumentException("File format MUST be PDF");
//
//            tmpFile = Files.createTempFile(nameWithId.replace(".pdf","ID"), ".pdf");
//
//            LOGGER.log(Level.INFO, String.format("Temp file %s created", tmpFile));
//            FileUtils.copyInputStreamToFile(stream, tmpFile.toFile());
//        } catch (IOException e) {
//            LOGGER.log(Level.SEVERE, "Exception occur", e);
//        }
//        return new PDFConverter(tmpFile.toString());
//    }

    public static PDFConverter newPDFConverter(InputStream stream, String id, String originalFileName, String endFolder) {
        Path tmpFile=null;
        try {

            if (!originalFileName.endsWith(".pdf")) throw new IllegalArgumentException("File format MUST be PDF");

            tmpFile = Files.createTempFile(originalFileName.replace(".pdf","_"), ".pdf");

            LOGGER.log(Level.INFO, String.format("Temp file %s created", tmpFile));
            FileUtils.copyInputStreamToFile(stream, tmpFile.toFile());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception occur", e);
        }
        return new PDFConverter(tmpFile.toString(), id, originalFileName, endFolder);
    }


    public static PDFConverter newPDFConverter(String absolutePath) {
        if (!absolutePath.endsWith(".pdf")) throw new IllegalArgumentException("File format MUST be PDF");
        return new PDFConverter(absolutePath);
    }
}
