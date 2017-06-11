package com.borovyksv.camel.processor;

import com.borovyksv.mongo.DocumentAdapter;
import com.borovyksv.mongo.observer.*;
import com.borovyksv.mongo.pojo.DocumentWithTextPages;
import com.borovyksv.mongo.pojo.ProgressStatus;
import com.borovyksv.mongo.repository.ConvertedDocumentRepository;
import com.borovyksv.mongo.repository.DocumentWithTextPagesRepository;
import com.borovyksv.mongo.repository.ProgressStatusRepository;
import com.borovyksv.util.PDFConverter;
import com.borovyksv.util.PDFConverterFactory;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class PDFProcessor implements Processor {
  @Autowired
  ProgressStatusRepository progressStatusRepository;
  @Autowired
  ConvertedDocumentRepository convertedDocumentRepository;
  @Autowired
  DocumentWithTextPagesRepository documentWithTextPagesRepository;

  @Value( "${end.folder}" )
  private String endFolder;

  public void process(Exchange exchange) throws Exception {
    InputStream stream = exchange.getIn().getBody(InputStream.class);
    String nameWithId = exchange.getIn().getHeader(Exchange.FILE_NAME, String.class);

    String id = nameWithId.replace(".pdf", "");

    DocumentWithTextPages databaseDocument = getDatabaseDocument(id);

    String originalFileName = databaseDocument.getName();

    PDFConverter converter = PDFConverterFactory.newPDFConverter(stream, id, originalFileName, endFolder);

    registerDBUpdateObservers(converter, originalFileName);

    converter.convert();


//    exchange.getOut().setHeader(Exchange.FILE_NAME, fileName);
//    exchange.getOut().setBody(converter.getResultFolder());

    databaseDocument.setPages(DocumentAdapter.getPageListFromMap(converter.getTextPages()));
    databaseDocument.setBookmarks(DocumentAdapter.getBookmarkListFromMap(converter.getBookmarkPages()));
    exchange.getOut().setBody(databaseDocument);

//    updateDocumentInDB(databaseDocument, converter);

  }

  private DocumentWithTextPages getDatabaseDocument(String id) {
    DocumentWithTextPages databaseDocument = documentWithTextPagesRepository.findById(id);
    if (databaseDocument==null){
      return new DocumentWithTextPages(id+".pdf");
    } else {
      return databaseDocument;
    }
  }


  private void registerDBUpdateObservers(PDFConverter converter, String fileName) {
    ProgressStatus progressStatus = new ProgressStatus(fileName);
    converter.addObserver(new PagesProcessObserver(progressStatusRepository, progressStatus));
    converter.addObserver(new ImageProcessObserver(progressStatusRepository, progressStatus));
    converter.addObserver(new TextProcessObserver(progressStatusRepository, progressStatus));
    converter.addObserver(new ErrorsObserver(progressStatusRepository, progressStatus));
    converter.addObserver(new IsSuccessfulObserver(progressStatusRepository, convertedDocumentRepository, progressStatus));

  }

//  private void updateDocumentInDB(DocumentWithTextPages databaseDocument, PDFConverter converter) {
//
//
//
//
//    documentWithTextPagesRepository.save(databaseDocument);
//
//  }


}
