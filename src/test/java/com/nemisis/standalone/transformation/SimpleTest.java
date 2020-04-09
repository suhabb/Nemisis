package com.nemisis.standalone.transformation;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class SimpleTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new SimpleRoute();
    }

    @Test
    public void testSimple() throws Exception {
        String response = template.requestBody("direct:start", "Camel Rocks", String.class);

        assertEquals("Hello Camel Rocks", response);
    }


    @Test
    public void testSimpleHeader() throws Exception {
        Object o = template.requestBodyAndHeader("direct:hello", "Camel Rocks", "user", "Neo");

        assertEquals("Hello Neo how are you?", o.toString());
    }
}