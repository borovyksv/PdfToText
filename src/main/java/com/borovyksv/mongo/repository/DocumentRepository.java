package com.borovyksv.mongo.repository;

import com.borovyksv.mongo.pojo.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<Document, String> {

    Document findByName(String name);

}
