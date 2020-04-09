package com.nemisis.standalone.route;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class RecipientListTest extends CamelTestSupport {

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @EndpointInject(uri = "mock:order.priority")
    private MockEndpoint orderPriority;

    @EndpointInject(uri = "mock:order.normal")
    private MockEndpoint orderNormal;

    @EndpointInject(uri = "mock:billing")
    private MockEndpoint billing;

    @EndpointInject(uri = "mock:unrecognized")
    private MockEndpoint unrecognized;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RecipientListRoute();
    }

    @Test
    public void testNormalOrder() throws InterruptedException {
        String messageBody = "book";

        billing.setExpectedMessageCount(1);
        orderNormal.setExpectedMessageCount(1);
        orderPriority.setExpectedMessageCount(0);
        unrecognized.setExpectedMessageCount(0);

        template.sendBodyAndHeader(messageBody, "orderType", "normal");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testPriorityOrder() throws InterruptedException {
        String messageBody = "book";

        billing.setExpectedMessageCount(1);
        orderNormal.setExpectedMessageCount(0);
        orderPriority.setExpectedMessageCount(1);
        unrecognized.setExpectedMessageCount(0);

        template.sendBodyAndHeader(messageBody, "orderType", "priority");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testUnknownOrder() throws InterruptedException {
        String messageBody = "book";

        billing.setExpectedMessageCount(0);
        orderNormal.setExpectedMessageCount(0);
        orderPriority.setExpectedMessageCount(0);
        unrecognized.setExpectedMessageCount(1);

        template.sendBody(messageBody);

        assertMockEndpointsSatisfied();
    }
}