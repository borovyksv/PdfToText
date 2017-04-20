package com.borovyksv.mongo.observer;

import com.borovyksv.mongo.pojo.ProgressStatus;
import com.borovyksv.mongo.repository.ProgressStatusRepository;
import com.borovyksv.util.PDFConverter;

public class ImageProcessObserver implements Observer<PDFConverter> {

  private ProgressStatusRepository progressStatusRepository;
  private ProgressStatus progressStatus;

  public ImageProcessObserver(ProgressStatusRepository progressStatusRepository, ProgressStatus progressStatus) {
    this.progressStatusRepository = progressStatusRepository;
    this.progressStatus = progressStatus;
  }

  @Override
  public void update(PDFConverter converter) {
      progressStatus.setImagesProgress(converter.getImagesProgress());
      progressStatusRepository.save(progressStatus);
  }
}
