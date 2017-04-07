package com.borovyksv.route;

import com.borovyksv.processor.PDFProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class PDFRoute extends RouteBuilder {
 
    @Override
    public void configure() throws Exception {
    	from("{{input.file.endpoint}}?maxMessagesPerPoll=1")
                .process(new PDFProcessor())
                .end();
    }
}
