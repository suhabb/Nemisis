package com.nemisis.standalone.parallel_process;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Test class that demonstrates the behavior of an {@link org.apache.camel.AsyncProcessor}.
 */
public class AsyncProcessorTest extends CamelTestSupport {

    final int messageCount = 10;

    @Override
    public RouteBuilder createRouteBuilder() {
        return new AsyncProcessorRoute();
    }

    @Test
    public void testAsyncProcessing() throws InterruptedException {

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(messageCount);
        mockOut.setResultWaitTime(5000);

        for (int i = 0; i < messageCount; i++) {
            template.sendBody("seda:in", ExchangePattern.InOnly, "Message[" + i + "]");
        }

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testSyncProcessing() throws InterruptedException {
        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(messageCount);
        mockOut.setResultWaitTime(5000);

        for (int i = 0; i < messageCount; i++) {
            template.sendBody("direct:sync", ExchangePattern.InOnly, "Message[" + i + "]");
        }

        assertMockEndpointsSatisfied();
    }
}