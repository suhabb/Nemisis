package com.nemisis.standalone.transformation;

import org.apache.camel.builder.RouteBuilder;

//https://camel.apache.org/components/latest/languages/simple-language.html
public class SimpleRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start")
                .transform(simple("Hello ${body}"));


        from("direct:hello")
                .transform().simple("Hello ${in.header.user} how are you?")
                .to("mock:reply");
    }
}