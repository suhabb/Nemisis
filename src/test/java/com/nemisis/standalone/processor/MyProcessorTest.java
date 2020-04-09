package com.nemisis.standalone.processor;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class MyProcessorTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new MyProcessorRoute();
    }

    @Test
    public void testMyProcessor() throws Exception {

        getMockEndpoint("mock:result").expectedBodiesReceived("Hello Scott");
        template.sendBodyAndHeader("direct:start", "Scott", "language", "en");//uri,body,headerKey,headerValue
        assertMockEndpointsSatisfied();
    }

    @Test
    public void testMyProcessor2() throws Exception {

        getMockEndpoint("mock:result").expectedBodiesReceived("Bonjour Scott");
        template.sendBodyAndHeader("direct:start", "Scott", "language", "fr");
        assertMockEndpointsSatisfied();
    }
}