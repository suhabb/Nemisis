package com.nemisis.standalone.splitter_aggregation;

import org.apache.camel.builder.RouteBuilder;

public class SplitMultiLineRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:in")
            .split(body().tokenize("\n"))
                .to("mock:out")
            .end();
    }
}