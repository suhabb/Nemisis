package com.nemisis.standalone.route;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class CallingInOnlyTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:beforeOneWay")
    private MockEndpoint beforeOneWay;

    @EndpointInject(uri = "mock:oneWay")
    private MockEndpoint oneWay;

    @EndpointInject(uri = "mock:afterOneWay")
    private MockEndpoint afterOneWay;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new CallingInOnlyViaToRoute();
    }

    @Test
    public void testInOutMEPChangedForOneWay() throws InterruptedException {

        final String messageBody = "Camel Rocks";
        final ExchangePattern callingMEP = ExchangePattern.InOut;

        beforeOneWay.setExpectedMessageCount(1);
        // Should be calling Exchange Pattern
        beforeOneWay.message(0).exchangePattern().isEqualTo(callingMEP);

        oneWay.setExpectedMessageCount(1);
        // Should always be InOnly
        oneWay.message(0).exchangePattern().isEqualTo(ExchangePattern.InOnly);

        afterOneWay.setExpectedMessageCount(1);
        // Should be restored to calling Exchange Pattern
        afterOneWay.message(0).exchangePattern().isEqualTo(callingMEP);

        // requestBody always sets the Exchange Pattern to InOut
        String response = template.requestBody("direct:start", messageBody, String.class);

        assertEquals("Done", response);

        assertMockEndpointsSatisfied();

        Exchange oneWayExchange = oneWay.getReceivedExchanges().get(0);
        Exchange afterOneWayExchange = afterOneWay.getReceivedExchanges().get(0);

        // these are not the same exchange objects
        assertNotEquals(oneWayExchange, afterOneWayExchange);

        // the bodies should be the same - shallow copy
        assertEquals(oneWayExchange.getIn().getBody(), afterOneWayExchange.getIn().getBody());

        // the transactions are the same
        assertEquals(oneWayExchange.getUnitOfWork(), afterOneWayExchange.getUnitOfWork());
    }

    @Test
    public void testInOnlyMEPChangedForOneWay() throws InterruptedException {
        final String messageBody = "Camel Rocks";
        final ExchangePattern callingMEP = ExchangePattern.InOnly;

        beforeOneWay.setExpectedMessageCount(1);
        // Should be calling Exchange Pattern
        beforeOneWay.message(0).exchangePattern().isEqualTo(callingMEP);

        oneWay.setExpectedMessageCount(1);
        // Should always be InOnly
        oneWay.message(0).exchangePattern().isEqualTo(ExchangePattern.InOnly);

        afterOneWay.setExpectedMessageCount(1);
        // Should be restored to calling Exchange Pattern
        afterOneWay.message(0).exchangePattern().isEqualTo(callingMEP);

        // Explicitly set Exchange Pattern
        template.sendBody("direct:start", callingMEP, messageBody);

        assertMockEndpointsSatisfied();

        Exchange oneWayExchange = oneWay.getReceivedExchanges().get(0);
        Exchange afterOneWayExchange = afterOneWay.getReceivedExchanges().get(0);

        // these are not the same exchange objects
        assertNotEquals(oneWayExchange, afterOneWayExchange);

        // the bodies should be the same - shallow copy
        assertEquals(oneWayExchange.getIn().getBody(), afterOneWayExchange.getIn().getBody());

        // the transactions are the same
        assertEquals(oneWayExchange.getUnitOfWork(), afterOneWayExchange.getUnitOfWork());
    }
}