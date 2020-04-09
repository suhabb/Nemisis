package com.nemisis.standalone.processor;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class MyProcessorInlineTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new MyProcessorInlineRoute();
    }

    @Test
    public void testMyProcessor() throws Exception {
        getMockEndpoint("mock:result").expectedBodiesReceived("Hello Scott");

        template.sendBodyAndHeader("direct:start", "Scott", "language", "en");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testMyProcessor2() throws Exception {
        getMockEndpoint("mock:result").expectedBodiesReceived("Bonjour Scott");

        template.sendBodyAndHeader("direct:start", "Scott", "language", "fr");

        assertMockEndpointsSatisfied();
    }
}