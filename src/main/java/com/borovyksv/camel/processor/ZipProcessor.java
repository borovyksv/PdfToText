package com.borovyksv.camel.processor;

import com.borovyksv.util.PDFConverter;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class ZipProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        PDFConverter converter = exchange.getIn().getBody(PDFConverter.class);
        String fileName = exchange.getIn().getHeader(Exchange.FILE_NAME, String.class);

        sendZippedFilesWithBody(exchange, fileName, converter);
    }

    private void sendZippedFilesWithBody(Exchange exchange, String fileName, PDFConverter converter) throws IOException {
        String zipFileName = fileName.replace(".pdf", ".zip");
        ZipFile convertedFile = zip(converter.getResultFolder(), zipFileName);

        exchange.getOut().setHeader(Exchange.FILE_NAME, zipFileName);
        exchange.getOut().setBody(convertedFile.getFile());

    }

    private synchronized ZipFile zip(String source, String fileName) {

        ZipFile zipFile = null;
        try {
            Path tempDirectory = Files.createTempDirectory("");

            String resultFile = tempDirectory.toString() + File.separatorChar + fileName;
            zipFile = new ZipFile(resultFile);
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setIncludeRootFolder(false);
            zipFile.addFolder(source, zipParameters);

        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return zipFile;
    }


}
