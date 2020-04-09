package com.nemisis.standalone.route;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class LoadBalancerStickyTest extends CamelTestSupport {

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @EndpointInject(uri = "mock:first")
    private MockEndpoint first;

    @EndpointInject(uri = "mock:second")
    private MockEndpoint second;

    @EndpointInject(uri = "mock:third")
    private MockEndpoint third;

    @EndpointInject(uri = "mock:out")
    private MockEndpoint out;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new LoadBalancerStickyRoute();
    }

    @Test
    public void testMessageLoadBalancedToStickyEndpoints() throws InterruptedException {
        final String messageBody = "Client has bought something";

        first.setExpectedMessageCount(4);
        first.message(0).header("customerId").isEqualTo(0);
        first.message(1).header("customerId").isEqualTo(3);
        first.message(2).header("customerId").isEqualTo(0);
        first.message(3).header("customerId").isEqualTo(3);

        second.setExpectedMessageCount(2);
        second.message(0).header("customerId").isEqualTo(1);
        second.message(1).header("customerId").isEqualTo(1);

        third.setExpectedMessageCount(2);
        third.message(0).header("customerId").isEqualTo(2);
        third.message(1).header("customerId").isEqualTo(2);

        for (int messageCount = 0; messageCount < 2; messageCount++) {
            for (int customerCount = 0; customerCount < 4; customerCount++) {
                template.sendBodyAndHeader(messageBody, "customerId", customerCount);
            }
        }

        assertMockEndpointsSatisfied();
    }

}