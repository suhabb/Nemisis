

package com.nemisis.standalone.testing.java;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.seda.SedaComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Test class that demonstrates how a custom CamelContext can be configured for a test run.
 */
public class CustomCamelContextConfigTest extends CamelTestSupport {

    @Override
    public CamelContext createCamelContext() {
        CamelContext context = new DefaultCamelContext();
        // plug in a seda component, as we don't really need an embedded broker
        context.addComponent("activemq", new SedaComponent());
        return context;
    }

    @Override
    public RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:in")
                    .to("activemq:orders");

                from("activemq:orders")
                    .to("mock:out");
            }
        };
    }

    @Test
    public void testMessagesFlowOverQueue() throws InterruptedException {

        MockEndpoint out = getMockEndpoint("mock:out");
        out.setExpectedMessageCount(1);
        out.expectedBodiesReceived("hello");

        template.sendBody("direct:in", "hello");

        assertMockEndpointsSatisfied();
    }
}
