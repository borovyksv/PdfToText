package com.borovyksv.mongo.pojo;

public class Page {

    public Integer pageNum;
    public String text;

  public Page(Integer pageNum, String text) {
    this.pageNum = pageNum;
    this.text = text;
  }

  public Page() {

    }

  public Integer getPageNum() {
    return pageNum;
  }

  public void setPageNum(Integer pageNum) {
    this.pageNum = pageNum;
  }

  public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
