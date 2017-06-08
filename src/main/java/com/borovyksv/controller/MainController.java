package com.borovyksv.controller;

import com.borovyksv.mongo.pojo.ConvertedDocument;
import com.borovyksv.mongo.pojo.ProgressStatus;
import com.borovyksv.mongo.repository.ConvertedDocumentRepository;
import com.borovyksv.mongo.repository.DocumentWithTextPagesRepository;
import com.borovyksv.mongo.repository.ProgressStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import java.util.List;

@RestController
public class MainController {

  @Autowired
  ProgressStatusRepository progressStatusRepository;
  @Autowired
  ConvertedDocumentRepository convertedDocumentRepository;
  @Autowired
  DocumentWithTextPagesRepository documentWithTextPagesRepository;
  @Autowired
  ServletContext servletContext;


//  @RequestMapping(value = "/documents/{keyword}", method = RequestMethod.GET)
//  public List<DocumentWithTextPages> search(@PathVariable( "keyword" ) String keyword) {
//    return documentWithTextPagesRepository.search(keyword);
//  }

  @RequestMapping(value = "/documents/progress", method = RequestMethod.GET)
  public List<ProgressStatus> progressStatus() {
    return progressStatusRepository.findAll();
  }

  @RequestMapping(value = "/documents/converted", method = RequestMethod.GET)
  public List<ConvertedDocument> convertedDocuments() {
    return convertedDocumentRepository.findAll();
  }


}
