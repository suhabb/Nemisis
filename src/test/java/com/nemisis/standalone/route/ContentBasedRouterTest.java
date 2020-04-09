package com.nemisis.standalone.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class ContentBasedRouterTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new ContentBasedRouterRoute();
    }

    @Test
    public void testWhen() throws Exception {
        getMockEndpoint("mock:camel").expectedMessageCount(1);
        getMockEndpoint("mock:other").expectedMessageCount(0);

        template.sendBody("direct:start", "Camel Rocks");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testOther() throws Exception {
        getMockEndpoint("mock:camel").expectedMessageCount(0);
        getMockEndpoint("mock:other").expectedMessageCount(1);

        template.sendBody("direct:start", "Hello World");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void giveTestOrderItemPizza() throws Exception {
        getMockEndpoint("mock:pizza").expectedMessageCount(1);
        getMockEndpoint("mock:cheese").expectedMessageCount(0);

        template.sendBodyAndHeader("direct:orderItem", "Pizza", "type", "pizza");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void giveTestOrderItemCheese() throws Exception {
        getMockEndpoint("mock:pizza").expectedMessageCount(0);
        getMockEndpoint("mock:cheese").expectedMessageCount(1);

        template.sendBodyAndHeader("direct:orderItem", "Pizza", "type", "cheese");

        assertMockEndpointsSatisfied();
    }
}