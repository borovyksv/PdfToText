package com.borovyksv.mongo.repository;

import com.borovyksv.mongo.pojo.ConvertedDocument;
import com.borovyksv.mongo.pojo.DocumentWithTextPages;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConvertedDocumentRepository extends MongoRepository<ConvertedDocument, String> {

  DocumentWithTextPages findByName(String name);

}
