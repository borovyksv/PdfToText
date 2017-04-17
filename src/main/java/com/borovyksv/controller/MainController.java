package com.borovyksv.controller;

import com.borovyksv.mongo.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

  @RequestMapping("/hello")
  public Page greeting() {
    return new Page(1, "THE PAGE");
  }
}
