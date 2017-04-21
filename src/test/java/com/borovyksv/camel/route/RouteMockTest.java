package com.borovyksv.camel.route;

import com.borovyksv.mongo.pojo.DocumentWithTextPages;
import com.borovyksv.mongo.repository.DocumentWithTextPagesRepository;
import com.borovyksv.mongo.pojo.Page;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RouteMockTest extends CamelTestSupport {

  @Autowired
  DocumentWithTextPagesRepository repository;

  @Override
  protected RoutesBuilder createRouteBuilder() throws Exception {
    return new RouteBuilder() {
      @Override
      public void configure() throws Exception {
        from("file:filterStart")
          .log("Received ${header.CamelFileName} from file:filterStart")
          .filter(header("CamelFileName").endsWith(".pdf"))
          .to("mock:fileFilterEndPoint");

        from("file:start")
          .log("Received ${header.CamelFileName} from file:start")
          .process(exchange -> exchange.getOut().setBody(exchange.getIn().getBody().toString().replace(".pdf", ".zip"))) //convert PDF to Zip
          .log("Sent ${body} to mock:fileEndPoint")
          .to("mock:fileEndPoint");

        from("direct:database")
          .log("Saving ${body} to DB")
          .bean(repository, "save")
          .to("mock:dbEndpoint");
      }
    };
  }

  @Test
  public void testFileConvertingFromPDFtoZIP() throws InterruptedException {
    MockEndpoint mock = getMockEndpoint("mock:fileEndPoint");
    mock.expectedBodiesReceived("GenericFile[hello.zip]");

    template.sendBodyAndHeader("file:start", "Hello World", Exchange.FILE_NAME, "hello.pdf");

    mock.assertIsSatisfied();
  }
  @Test
  public void testRouteFilterNoPDFFiles() throws InterruptedException {
    MockEndpoint mock = getMockEndpoint("mock:fileFilterEndPoint");
    mock.expectedMessageCount(0);

    template.sendBodyAndHeader("file:filterStart", "Hello World", Exchange.FILE_NAME, "hello.txt");
    template.sendBodyAndHeader("file:filterStart", "Hello World", Exchange.FILE_NAME, "hello.txt");
    template.sendBodyAndHeader("file:filterStart", "Hello World", Exchange.FILE_NAME, "hello.zip");
    template.sendBodyAndHeader("file:filterStart", "Hello World", Exchange.FILE_NAME, "hello.zip");
    template.sendBodyAndHeader("file:filterStart", "Hello World", Exchange.FILE_NAME, "hello.jpg");
    template.sendBodyAndHeader("file:filterStart", "Hello World", Exchange.FILE_NAME, "hello.png");
    mock.assertIsSatisfied();
  }

  @Test
  public void testRepositoryRoute() throws InterruptedException {
    DocumentWithTextPages document = new DocumentWithTextPages(Arrays.asList(new Page(1, "1")), "Test");

    MockEndpoint mock = getMockEndpoint("mock:dbEndpoint");

    mock.expectedBodiesReceived(document);
    mock.expectedMessageCount(1);

    template.sendBody("direct:database", document);

    TimeUnit.SECONDS.sleep(2);
    mock.assertIsSatisfied();
  }


}
