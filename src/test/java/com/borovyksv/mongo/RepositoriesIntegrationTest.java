package com.borovyksv.mongo;
/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.borovyksv.mongo.pojo.ConvertedDocument;
import com.borovyksv.mongo.pojo.ProgressStatus;
import com.borovyksv.mongo.repository.ConvertedDocumentRepository;
import com.borovyksv.mongo.repository.ProgressStatusRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoriesIntegrationTest {

  @Autowired
  ProgressStatusRepository progressStatusRepository;
  @Autowired
  ConvertedDocumentRepository convertedDocumentRepository;

  @Test
  public void moveFromProgressStatusRepositoryToConvertedDocumentRepositoryTest() throws InterruptedException {
    ProgressStatus testDoc = progressStatusRepository.save(new ProgressStatus("Test"));
    Integer testNumberOfPages = 100;

    progressStatusRepository.delete(testDoc);

    ConvertedDocument convertedDocument = new ConvertedDocument(
      testDoc.getDocName(),
      testNumberOfPages
    );
    ConvertedDocument movedDoc = convertedDocumentRepository.save(convertedDocument);

    assertThat(progressStatusRepository.findOne(testDoc.getId())).isNull();
    assertThat(testDoc.getDocName()).isEqualTo(movedDoc.getName());

    convertedDocumentRepository.delete(convertedDocument);


  }




}
