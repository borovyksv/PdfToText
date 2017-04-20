package com.borovyksv.mongo.pojo;

import org.springframework.data.annotation.Id;

public class ProgressStatus {
  @Id
  public String id;
  public String docName;

  public Integer pagesProgress;
  public Integer imagesProgress;
  public Integer textProgress;

  public ProgressStatus() {
  }

  public ProgressStatus(String docName, Integer pagesProgress, Integer imagesProgress, Integer textProgress) {
    this.docName = docName;
    this.pagesProgress = pagesProgress;
    this.imagesProgress = imagesProgress;
    this.textProgress = textProgress;
  }

  public ProgressStatus(String docName) {
    this.docName = docName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDocName() {
    return docName;
  }

  public void setDocName(String docName) {
    this.docName = docName;
  }

  public Integer getPagesProgress() {
    return pagesProgress;
  }

  public void setPagesProgress(Integer pagesProgress) {
    this.pagesProgress = pagesProgress;
  }

  public Integer getImagesProgress() {
    return imagesProgress;
  }

  public void setImagesProgress(Integer imagesProgress) {
    this.imagesProgress = imagesProgress;
  }

  public Integer getTextProgress() {
    return textProgress;
  }

  public void setTextProgress(Integer textProgress) {
    this.textProgress = textProgress;
  }

  @Override
  public String toString() {
    return "ProgressStatus{" +
      "Document='" + docName + '\'' +
      ", pages=" + pagesProgress +
      "%, images=" + imagesProgress +
      "%, text=" + textProgress +
      "%}";
  }
}


