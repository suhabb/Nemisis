package com.nemisis.standalone.transformation.enrich;

import org.apache.camel.builder.RouteBuilder;

public class EnrichRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:start")
            .enrich("bean:myExpander?method=expand");
    }
}