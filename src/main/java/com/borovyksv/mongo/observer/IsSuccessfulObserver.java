package com.borovyksv.mongo.observer;

import com.borovyksv.mongo.pojo.ConvertedDocument;
import com.borovyksv.mongo.pojo.ProgressStatus;
import com.borovyksv.mongo.repository.ConvertedDocumentRepository;
import com.borovyksv.mongo.repository.ProgressStatusRepository;
import com.borovyksv.util.PDFConverter;

public class IsSuccessfulObserver implements Observer<PDFConverter> {


  private ConvertedDocumentRepository convertedDocumentRepository;
  private ProgressStatusRepository progressStatusRepository;

  private ProgressStatus progressStatus;

  public IsSuccessfulObserver(ProgressStatusRepository progressStatusRepository, ConvertedDocumentRepository convertedDocumentRepository, ProgressStatus progressStatus) {
    this.progressStatusRepository = progressStatusRepository;
    this.convertedDocumentRepository = convertedDocumentRepository;
    this.progressStatus = progressStatus;
  }

  @Override
  public void update(PDFConverter converter) {
    if (converter.isConverted()) {
      progressStatusRepository.delete(progressStatus);

      ConvertedDocument convertedDocument = new ConvertedDocument(
        progressStatus.getDocName(),
        converter.getNumberOfPages()
      );
      convertedDocumentRepository.save(convertedDocument);
    }
  }
}
