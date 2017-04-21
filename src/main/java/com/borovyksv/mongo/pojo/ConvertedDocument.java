package com.borovyksv.mongo.pojo;

import org.springframework.data.annotation.Id;


public class ConvertedDocument {

  @Id
  public String id;

  public String name;
  public Integer numberOfPages;

  public ConvertedDocument(String name, Integer numberOfPages) {

    this.name = name;
    this.numberOfPages = numberOfPages;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getNumberOfPages() {
    return numberOfPages;
  }

  public void setNumberOfPages(Integer numberOfPages) {
    this.numberOfPages = numberOfPages;
  }


}

