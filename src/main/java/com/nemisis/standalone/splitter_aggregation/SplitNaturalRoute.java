package com.nemisis.standalone.splitter_aggregation;

import org.apache.camel.builder.RouteBuilder;

public class SplitNaturalRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:in")
            .split(body())
                .log("Body : ${body}")
                .to("mock:split")
            .end()
            .to("mock:out");

    }
}