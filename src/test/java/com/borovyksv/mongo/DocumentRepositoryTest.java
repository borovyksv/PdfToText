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

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DocumentRepositoryTest {

    @Autowired
    DocumentRepository repository;

    Document doc1, doc2, doc3;

    @Before
    public void setUp() {

        doc1 = repository.save(getDocument("TEST 1"));
        doc2 = repository.save(getDocument("TEST 2"));
        doc3 = repository.save(getDocument("TEST 3"));
    }


    @Test
    public void setsIdOnSave() {

        Document test = repository.save(getDocument("TEST 4"));

        assertThat(test.id).isNotNull();
        repository.delete(test);
    }

    @Test
    public void findsByName() {

        Document test = repository.findByName("TEST 2");

        assertThat(test.getName()).isEqualToIgnoringCase(doc2.getName());
    }

    @After
    public  void tearDown(){
        repository.delete(doc1);
        repository.delete(doc2);
        repository.delete(doc3);
    }


    @Ignore
    private Document getDocument(String name) {
        Document document = new Document();
        List<Page> pageList = new ArrayList<>();

        document.setName(name);

        for (int i = 0; i < 5; i++) {
            Page page = new Page();
            page.setId(i);
            page.setText(name+": TEST TEXT -"+i);
            pageList.add(page);
        }

        document.setPages(pageList);
        return document;
    }

}