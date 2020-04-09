package com.nemisis.standalone.transformation;

import com.nemisis.standalone.model.View;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class JsonJacksonRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:marshal")
            .marshal().json(JsonLibrary.Jackson)
            .to("mock:marshalResult");

        from("direct:unmarshal")
            .unmarshal().json(JsonLibrary.Jackson, View.class)
            .to("mock:unmarshalResult");
    }
}