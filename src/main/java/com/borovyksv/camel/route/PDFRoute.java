package com.borovyksv.camel.route;

import com.borovyksv.camel.processor.PDFProcessor;
import com.borovyksv.camel.processor.ZipProcessor;
import com.borovyksv.mongo.repository.DocumentRepository;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PDFRoute extends RouteBuilder {

    @Autowired
    ZipProcessor zipProcessor;
    @Autowired
    PDFProcessor pdfProcessor;
    @Autowired
    DocumentRepository repository;

    @Override
    public void configure() throws Exception {
        from("{{route.from}}?maxMessagesPerPoll=1")
                .filter(header("CamelFileName").endsWith(".pdf"))                                   //filter PDF input
                .process(pdfProcessor).log("Sending ${header.CamelFileName} to Zip process")        //convert PDF to files
                .to("seda:convertedPdf");

        from("seda:convertedPdf")
                .process(zipProcessor).log("Zipped ${header.CamelFileName} sending to {{route.to}}")//Zipping files
                .to("{{route.to}}");                                                                //Sending .zip to final destination


        from("seda:database")
                .log("Saving ${body} to DB")                                                        //save TXT pages to DB
                .bean(repository, "save");
    }
}
