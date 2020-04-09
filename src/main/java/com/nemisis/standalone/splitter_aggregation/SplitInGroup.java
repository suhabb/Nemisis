package com.nemisis.standalone.splitter_aggregation;

import org.apache.camel.builder.RouteBuilder;

public class SplitInGroup extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:inbox")
                .split().tokenize(",", 3).streaming()
                .log("Body is :${body}")
                .to("direct:order");

        from("direct:order")
                .log("Order is : ${body}")
                .to("mock:out");
    }
}