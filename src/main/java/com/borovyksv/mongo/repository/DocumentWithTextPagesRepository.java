package com.borovyksv.mongo.repository;

import com.borovyksv.mongo.pojo.DocumentWithTextPages;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentWithTextPagesRepository extends MongoRepository<DocumentWithTextPages, String> {
//    @Query("{$text:{$search:\"?0\"}}")
//    List<DocumentWithTextPages> search(String keyword);

    DocumentWithTextPages findByName(String name);

}
