package com.nemisis.standalone.transformation;

import com.nemisis.standalone.model.View;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class JsonRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:marshal")
            .marshal().json(JsonLibrary.XStream)
            .to("mock:marshalResult");

        from("direct:unmarshal")
            .unmarshal().json(JsonLibrary.XStream, View.class)
            .to("mock:unmarshalResult");
    }
}