//package com.borovyksv.camel.processor;
//
//import net.lingala.zip4j.core.ZipFile;
//import net.lingala.zip4j.exception.ZipException;
//import net.lingala.zip4j.model.ZipParameters;
//import org.apache.camel.Exchange;
//import org.apache.camel.Processor;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//
//@Component
//public class UnzipProcessor implements Processor {
//    @Override
//    public void process(Exchange exchange) throws Exception {
////      InputStream stream = exchange.getIn().getBody(InputStream.class);
////      String fileName = exchange.getIn().getHeader(Exchange.FILE_NAME, String.class);
//
//
//
//
//
//      String folder = exchange.getIn().getBody(String.class);
//        String fileName = exchange.getIn().getHeader(Exchange.FILE_NAME, String.class);
//
//        sendZippedFilesWithBody(exchange, fileName, folder);
//
//    }
//
//
//
//  private void sendZippedFilesWithBody(Exchange exchange, String fileName, String folder) throws IOException {
//        String zipFileName = fileName.replace(".pdf", ".zip");
//        ZipFile convertedFile = zip(folder, zipFileName);
//
//        exchange.getOut().setHeader(Exchange.FILE_NAME, zipFileName);
//        exchange.getOut().setBody(convertedFile.getFile());
//
//    }
//
//    private ZipFile zip(String source, String fileName) {
//
//        ZipFile zipFile = null;
//        try {
//            Path tempDirectory = Files.createTempDirectory("");
//
//            String resultFile = tempDirectory.toString() + File.separatorChar + fileName;
//            zipFile = new ZipFile(resultFile);
//            ZipParameters zipParameters = new ZipParameters();
//            zipParameters.setIncludeRootFolder(false);
//            zipFile.addFolder(source, zipParameters);
//
//        } catch (ZipException | IOException e) {
//            e.printStackTrace();
//        }
//
//      return zipFile;
//    }
//
//
//}
