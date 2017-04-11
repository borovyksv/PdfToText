package com.borovyksv.camel.processor;

import com.borovyksv.mongo.Document;
import com.borovyksv.mongo.Page;
import com.borovyksv.util.PDFConverter;
import com.borovyksv.util.PDFConverterFactory;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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


        sendZippedFilesToRoute(fileName, converter, "{{route.to}}", exchange);


        //set body to Document with txt files, and send to DB route
        Map<Integer, String> textPages = converter.getTextPages();
        Document document = getDocumentFromMap(fileName, textPages);
        exchange.getOut().setBody(document);
    }



    private void sendZippedFilesToRoute(String fileName, PDFConverter converter, String toRoute, Exchange exchange) throws IOException {
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

    private Document getDocumentFromMap(String fileName, Map<Integer, String> textPages) {
        Document document = new Document();
        List<Page> pageList = new ArrayList<>();


        String cuttedFileName = fileName.substring(0, fileName.lastIndexOf('.'));
        document.setName(cuttedFileName);

        for (Map.Entry<Integer, String> entry : textPages.entrySet()) {
            Page page = new Page();
            page.setId(entry.getKey());
            page.setText(entry.getValue());
            pageList.add(page);
        }

        document.setPages(pageList);
        return document;
    }
}
