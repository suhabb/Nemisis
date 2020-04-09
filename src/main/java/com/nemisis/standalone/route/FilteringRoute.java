package com.nemisis.standalone.route;

import org.apache.camel.builder.RouteBuilder;

public class FilteringRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start")
            .filter()
                .simple("${body} regex '^C.*'")
                    .to("mock:C")
            .end()
            .to("mock:afterC")
            .filter()
                .simple("${body} contains 'amel'")
                    .to("mock:amel")
            .end()
            .to("mock:other");
    }
}