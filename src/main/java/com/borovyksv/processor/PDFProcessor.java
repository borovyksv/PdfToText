package com.borovyksv.processor;

import com.borovyksv.util.PDFConverter;
import com.borovyksv.util.PDFConverterFactory;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.io.InputStream;

public class PDFProcessor implements Processor{
    public void process(Exchange exchange) throws Exception {
        InputStream stream = exchange.getIn().getBody(InputStream.class);
        String fileName = exchange.getIn().getHeader("CamelFileName", String.class);

        PDFConverter converter = PDFConverterFactory.newPDFConverter(stream, fileName);
        converter.convert();
    }
}
