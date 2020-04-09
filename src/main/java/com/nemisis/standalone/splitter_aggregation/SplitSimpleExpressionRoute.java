package com.nemisis.standalone.splitter_aggregation;

import org.apache.camel.builder.RouteBuilder;

public class SplitSimpleExpressionRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:in")
                .split(simple("${body.wrapped}"))
                .to("mock:out")
                .end();

        from("direct:wrapper")
                .split(simple("${body.wrapperName}"))
                .to("mock:out")
                .end();
    }
}