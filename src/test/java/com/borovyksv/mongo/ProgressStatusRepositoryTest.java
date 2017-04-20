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

import com.borovyksv.mongo.pojo.ProgressStatus;
import com.borovyksv.mongo.repository.ProgressStatusRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProgressStatusRepositoryTest {

  @Autowired
  ProgressStatusRepository repository;

  ProgressStatus doc1, doc2, doc3;

  @Before
  public void setUp() {

    doc1 = repository.save(new ProgressStatus("TEST PDF", 50, 90, 15));
    doc2 = repository.save(new ProgressStatus("TEST PDF2", 100, 30, 35));
    doc3 = repository.save(new ProgressStatus("TEST PDF3", 20, 40, 85));
  }


  @Test
  public void setsIdOnSave() {

    ProgressStatus test = repository.save(new ProgressStatus("TEST PDF4", 100, 30, 35));

    assertThat(test.id).isNotNull();
    repository.delete(test);
  }

  @Test
  public void findsByName() {

    ProgressStatus test = repository.findByDocName("TEST PDF3");

    assertThat(test.getDocName()).isEqualToIgnoringCase(doc3.getDocName());
  }

  @Test
  public void updateDoc() {

    ProgressStatus testObject = repository.save(new ProgressStatus("TEST PDF5", 50, 90, 15));

    Integer expected = 100;
    testObject.setTextProgress(expected);

    repository.save(testObject);

    ProgressStatus dbObject = repository.findOne(testObject.getId());
    assertThat(expected).isEqualTo(dbObject.getTextProgress());
  }

    @After
    public  void tearDown(){
        repository.delete(doc1);
        repository.delete(doc2);
        repository.delete(doc3);
    }


}
