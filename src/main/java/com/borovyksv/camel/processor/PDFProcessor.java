package com.borovyksv.camel.processor;

import com.borovyksv.mongo.Document;
import com.borovyksv.mongo.DocumentAdapter;
import com.borovyksv.util.PDFConverter;
import com.borovyksv.util.PDFConverterFactory;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class PDFProcessor implements Processor {
    private static final Logger LOGGER = Logger.getLogger(PDFProcessor.class.getName());


    public void process(Exchange exchange) throws Exception {
        InputStream stream = exchange.getIn().getBody(InputStream.class);
        String fileName = exchange.getIn().getHeader("CamelFileName", String.class);

        PDFConverter converter = PDFConverterFactory.newPDFConverter(stream, fileName);
        converter.convert();


        sendZippedFilesWithBody(exchange, fileName, converter);

        //set body to (Document with txt files) and send to DB route
        sendTextPagesToDBRoute("direct:database", exchange, fileName, converter);

    }

    private void sendTextPagesToDBRoute(String toRoute, Exchange exchange, String fileName, PDFConverter converter) {
        Map<Integer, String> textPages = converter.getTextPages();
        Document document = DocumentAdapter.getDocumentFromMap(fileName, textPages);

        ProducerTemplate template = exchange.getContext().createProducerTemplate();
        template.sendBody(toRoute, document);
    }


    private void sendZippedFilesWithBody(Exchange exchange, String fileName, PDFConverter converter) throws IOException {
        String zipFileName = fileName.replace(".pdf", ".zip");
        ZipFile convertedFile = zip(converter.getResultFolder(), zipFileName);

        exchange.getOut().setBody(convertedFile.getFile());
        exchange.getOut().setHeader("CamelFileName", zipFileName);

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
