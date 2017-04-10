package com.borovyksv.camel.processor;

import com.borovyksv.mongo.Document;
import com.borovyksv.mongo.Page;
import com.borovyksv.util.PDFConverter;
import com.borovyksv.util.PDFConverterFactory;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PDFProcessor implements Processor {

    public void process(Exchange exchange) throws Exception {
        InputStream stream = exchange.getIn().getBody(InputStream.class);
        String fileName = exchange.getIn().getHeader("CamelFileName", String.class);

        PDFConverter converter = PDFConverterFactory.newPDFConverter(stream, fileName);
        converter.convert();


//



        //set body to Document with txt files, and send to DB route
        Map<Integer, String> textPages = converter.getTextPages();
        Document document = getDocumentFromMap(fileName, textPages);
        exchange.getOut().setBody(document);
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
