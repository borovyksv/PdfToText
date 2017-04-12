package com.borovyksv.camel.processor;

import com.borovyksv.mongo.Document;
import com.borovyksv.mongo.DocumentAdapter;
import com.borovyksv.util.PDFConverter;
import com.borovyksv.util.PDFConverterFactory;
import com.borovyksv.util.PDFConverterTest;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.io.FileUtils;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PDFProcessor implements Processor {
    private static final Logger LOGGER = Logger.getLogger(PDFProcessor.class.getName());


    public void process(Exchange exchange) throws Exception {
        InputStream stream = exchange.getIn().getBody(InputStream.class);
        String fileName = exchange.getIn().getHeader("CamelFileName", String.class);

        PDFConverter converter = PDFConverterFactory.newPDFConverter(stream, fileName);
        converter.convert();


        sendZippedFilesToRoute("{{route.to}}", fileName, converter, exchange);

        //set body to Document with txt files and send to DB route
        sendTextPagesWIthBody(exchange, fileName, converter);

//        runTests();
    }

    private void sendTextPagesWIthBody(Exchange exchange, String fileName, PDFConverter converter) {
        Map<Integer, String> textPages = converter.getTextPages();
        Document document = DocumentAdapter.getDocumentFromMap(fileName, textPages);
        exchange.getOut().setBody(document);
    }

    private void runTests() {
        Result result = JUnitCore.runClasses(PDFConverterTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }


    private void sendZippedFilesToRoute(String toRoute, String fileName, PDFConverter converter, Exchange exchange) throws IOException {
        String zipFileName = fileName.replace(".pdf", ".zip");
        ZipFile convertedFile = zip(converter.getResultFolder(), zipFileName);

        ProducerTemplate template = exchange.getContext().createProducerTemplate();
        template.sendBodyAndHeader(toRoute, FileUtils.openInputStream(convertedFile.getFile()),
                "CamelFileName", zipFileName);
        LOGGER.log(Level.INFO, String.format("%s sended to %s", zipFileName, toRoute));


    }

    private ZipFile zip(String source, String fileName) {
        String resultFile = Paths.get(source).getParent().toString()+ File.separator+fileName;

        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(resultFile);
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setIncludeRootFolder(false);
            zipFile.addFolder(source, zipParameters);
        } catch (ZipException e) {
            e.printStackTrace();
        }

        return zipFile;
    }

}
