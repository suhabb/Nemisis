package com.nemisis.standalone.parallel_process;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Test class that exercises parallel threads in Seda.
 */
public class EndpointConsumersSedaTest extends CamelTestSupport {

    @Override
    public RouteBuilder createRouteBuilder() {
        return new EndpointConsumersSedaRoute();
    }

    @Test
    public void testParallelConsumption() throws InterruptedException {

        final int messageCount = 100;
        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(messageCount);
        mockOut.setResultWaitTime(5000);

        for (int i = 0; i < messageCount; i++) {
            template.sendBody("seda:in", "Message[" + i + "]");
        }

        assertMockEndpointsSatisfied();
    }
}