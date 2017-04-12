package com.borovyksv.camel.route;

import com.borovyksv.camel.processor.PDFProcessor;
import com.borovyksv.mongo.DocumentRepository;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PDFRoute extends RouteBuilder {

    @Autowired
    PDFProcessor pdfProcessor;
    @Autowired
    DocumentRepository repository;

    @Override
    public void configure() throws Exception {
        from("{{route.from}}?maxMessagesPerPoll=1")
                .filter(header("CamelFileName").endsWith(".pdf"))                //filter PDF input
                .process(pdfProcessor).log("Sent ${headers} to {{route.to}}")    //convert PDF to files
                .to("{{route.to}}");

        from("direct:database")
                .log("Saving ${body} to DB")                                     //save TXT pages to DB
                .bean(repository, "save");
    }
}
