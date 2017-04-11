package com.borovyksv.camel.route;

import com.borovyksv.camel.processor.PDFProcessor;
import com.borovyksv.mongo.DocumentRepository;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PDFRoute extends RouteBuilder {
    @Autowired
    DocumentRepository repository;

    @Override
    public void configure() throws Exception {
        from("{{route.from}}?maxMessagesPerPoll=1")
                .process(new PDFProcessor())                            //convert PDF to files
                .log("Saving to DB ${body}").bean(repository, "save")   //save TXT pages to DB
                .log("Message Processed").end();
    }
}
