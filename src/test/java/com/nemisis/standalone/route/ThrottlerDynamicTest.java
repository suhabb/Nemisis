package com.nemisis.standalone.route;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class ThrottlerDynamicTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new ThrottlerDynamicRoute();
    }

    @Test
    public void testThrottleDynamic() throws Exception {
        final int throttleRate = 3;
        final int messageCount = throttleRate + 2;

        getMockEndpoint("mock:unthrottled").expectedMessageCount(messageCount);
        getMockEndpoint("mock:throttled").expectedMessageCount(throttleRate);
        getMockEndpoint("mock:after").expectedMessageCount(throttleRate);

        for (int i = 0; i < messageCount; i++) {
            Exchange exchange = getMandatoryEndpoint("direct:start").createExchange();
            {
                Message in = exchange.getIn();
                in.setHeader("throttleRate", throttleRate);
                in.setBody("Camel Rocks");
            }
            template.asyncSend("direct:start", exchange);
        }

        // the test will stop once all of the conditions have been met
        // the only way this set of conditions can happen is if 2
        // messages are currently suspended for throttling
        assertMockEndpointsSatisfied();
    }
}