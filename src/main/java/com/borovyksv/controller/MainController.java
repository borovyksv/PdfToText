package com.borovyksv.controller;

import com.borovyksv.mongo.pojo.ConvertedDocument;
import com.borovyksv.mongo.pojo.ProgressStatus;
import com.borovyksv.mongo.repository.ConvertedDocumentRepository;
import com.borovyksv.mongo.repository.DocumentWithTextPagesRepository;
import com.borovyksv.mongo.repository.ProgressStatusRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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

  @RequestMapping("/photo1")
  public void photo(HttpServletResponse response) throws IOException {
    response.setContentType("image/jpeg");
    InputStream in = servletContext.getResourceAsStream("D:\\pdf\\copy\\2k14acadia\\IMG\\1.jpg");
    IOUtils.copy(in, response.getOutputStream());
  }

}
