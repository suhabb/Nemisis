package com.nemisis.standalone.processor;

import org.apache.camel.builder.RouteBuilder;

public class MyProcessorRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:start")
            .process(new MyProcessor())
            .to("mock:result");
    }
}