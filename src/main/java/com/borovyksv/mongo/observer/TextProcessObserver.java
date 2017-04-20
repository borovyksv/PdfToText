package com.borovyksv.mongo.observer;

import com.borovyksv.mongo.pojo.ProgressStatus;
import com.borovyksv.mongo.repository.ProgressStatusRepository;
import com.borovyksv.util.PDFConverter;

public class TextProcessObserver implements Observer<PDFConverter> {

  private ProgressStatusRepository progressStatusRepository;
  private ProgressStatus progressStatus;

  public TextProcessObserver(ProgressStatusRepository progressStatusRepository, ProgressStatus progressStatus) {
    this.progressStatusRepository = progressStatusRepository;
    this.progressStatus = progressStatus;
  }

  @Override
  public void update(PDFConverter converter) {
      progressStatus.setTextProgress(converter.getTextProgress());
      progressStatusRepository.save(progressStatus);
  }
}
