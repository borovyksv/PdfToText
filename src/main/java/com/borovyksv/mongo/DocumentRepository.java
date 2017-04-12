package com.borovyksv.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<Document, String> {

    Document findByName(String name);

}
