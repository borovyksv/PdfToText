package com.borovyksv.camel.route;

import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class RouteMockTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:file/inbox?maxMessagesPerPoll=1").to("mock:fileEndPoint");
            }
        };
    }

    @Test
    public void testQuote() throws InterruptedException {
        MockEndpoint quote = getMockEndpoint("mock:fileEndPoint");
        quote.expectedMessageCount(2);

        template.sendBodyAndHeader("file:file/inbox", "Hello world", Exchange.FILE_NAME, "hello.pdf");
        template.sendBodyAndHeader("file:file/inbox", "Hello world2", Exchange.FILE_NAME, "hello2.pdf");

        TimeUnit.SECONDS.sleep(2);
        quote.assertIsSatisfied();
    }



}
