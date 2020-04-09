package com.nemisis.standalone.route;

import org.apache.camel.builder.RouteBuilder;




public class ContentBasedRouterRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        from("direct:start")
                .choice()
                .when()
                .simple("${body} contains 'Camel'")
                    .to("mock:camel")
                    .log("Camel ${body}")
                .otherwise()
                    .to("mock:other")
                    .log("Other ${body}")
                .end()
                .log("Message ${body}");

        from("direct:orderItem")
                .choice()
                .when(header("type").isEqualTo("pizza"))
                    .to("mock:pizza")
                .when(header("type").isEqualTo("cheese"))
                    .to("mock:cheese")
                .otherwise()
                    .to("mock:others")
                .end();

    }
}