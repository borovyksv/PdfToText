package com.borovyksv.mongo.repository;

import com.borovyksv.mongo.pojo.ProgressStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProgressStatusRepository extends MongoRepository<ProgressStatus, String> {

    ProgressStatus findByDocName(String docName);

}
