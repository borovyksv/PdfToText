package com.borovyksv.camel.processor;

import com.borovyksv.mongo.pojo.Document;
import com.borovyksv.mongo.DocumentAdapter;
import com.borovyksv.mongo.pojo.ProgressStatus;
import com.borovyksv.mongo.repository.ProgressStatusRepository;
import com.borovyksv.util.PDFConverter;
import com.borovyksv.util.PDFConverterFactory;
import com.borovyksv.mongo.observer.ImageProcessObserver;
import com.borovyksv.mongo.observer.PagesProcessObserver;
import com.borovyksv.mongo.observer.TextProcessObserver;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class PDFProcessor implements Processor {
  @Autowired
  ProgressStatusRepository progressStatusRepository;

  private static final Logger LOGGER = Logger.getLogger(PDFProcessor.class.getName());


  public void process(Exchange exchange) throws Exception {
    InputStream stream = exchange.getIn().getBody(InputStream.class);
    String fileName = exchange.getIn().getHeader(Exchange.FILE_NAME, String.class);

    PDFConverter converter = PDFConverterFactory.newPDFConverter(stream, fileName);

    registerDBUpdateObservers(converter, fileName);

    converter.convert();


    exchange.getOut().setHeader(Exchange.FILE_NAME, fileName);
    exchange.getOut().setBody(converter.getResultFolder());

    //set body to (Document with txt files) and send to DB route
    sendTextPagesToDBRoute("seda:database", exchange, fileName, converter);

  }

  private void registerDBUpdateObservers(PDFConverter converter, String fileName) {
    ProgressStatus progressStatus = new ProgressStatus(fileName);
    converter.addObserver(new PagesProcessObserver(progressStatusRepository, progressStatus));
    converter.addObserver(new ImageProcessObserver(progressStatusRepository, progressStatus));
    converter.addObserver(new TextProcessObserver(progressStatusRepository, progressStatus));

  }

  private void sendTextPagesToDBRoute(String toRoute, Exchange exchange, String fileName, PDFConverter converter) {
    Map<Integer, String> textPages = converter.getTextPages();
    Document document = DocumentAdapter.getDocumentFromMap(fileName, textPages);

    ProducerTemplate template = exchange.getContext().createProducerTemplate();
    template.sendBody(toRoute, document);
  }


}
