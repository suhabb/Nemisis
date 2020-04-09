package com.nemisis.standalone.parallel_process;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.List;

/**
 * Test class that exercises a custom thread pool using the threads DSL, using the routeBuilder() option in a
 * RouteBuilder.
 */
public class CustomThreadPoolInlineTest extends CamelTestSupport {

    @Override
    public RouteBuilder createRouteBuilder() {
        return new CustomThreadPoolInlineRoute();
    }

    @Test
    public void testProcessedByCustomThreadPool() throws InterruptedException {

        final int messageCount = 50;
        final MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(messageCount);
        mockOut.setResultWaitTime(6000);

        for (int i = 0; i < messageCount; i++) {
            template.asyncSendBody("direct:in", "Message[" + i + "]");
        }

        assertMockEndpointsSatisfied();
        List<Exchange> receivedExchanges = mockOut.getReceivedExchanges();
        for (Exchange exchange : receivedExchanges) {
            assertTrue(exchange.getIn().getBody(String.class).endsWith("CustomThreadPool"));
        }
    }
}