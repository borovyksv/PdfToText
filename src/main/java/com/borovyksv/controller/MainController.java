package com.borovyksv.controller;

import com.borovyksv.mongo.pojo.ProgressStatus;
import com.borovyksv.mongo.repository.ProgressStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {

  @Autowired
  ProgressStatusRepository progressStatusRepository;


  @RequestMapping(value = "/documents", method = RequestMethod.GET)
  public List<ProgressStatus> documents() {
    return progressStatusRepository.findAll();
  }
}
