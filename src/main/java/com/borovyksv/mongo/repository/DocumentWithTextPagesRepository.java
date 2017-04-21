package com.borovyksv.mongo.repository;

import com.borovyksv.mongo.pojo.DocumentWithTextPages;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentWithTextPagesRepository extends MongoRepository<DocumentWithTextPages, String> {

    DocumentWithTextPages findByName(String name);

}
