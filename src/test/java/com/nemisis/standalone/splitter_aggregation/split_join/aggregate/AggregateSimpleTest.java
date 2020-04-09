package com.nemisis.standalone.splitter_aggregation.split_join.aggregate;

import com.nemisis.standalone.splitter_aggregation.aggregation.AggregateSimpleRoute;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Test class that demonstrates a simple example of aggregation.
 */
public class AggregateSimpleTest extends CamelTestSupport {

    @Override
    public RouteBuilder createRouteBuilder() {
        return new AggregateSimpleRoute();
    }

    @Test
    public void testAggregation() throws InterruptedException {

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(2);

        template.sendBodyAndHeader("direct:in", "One", "group", "odd");
        template.sendBodyAndHeader("direct:in", "Two", "group", "even");
        template.sendBodyAndHeader("direct:in", "Three", "group", "odd");
        template.sendBodyAndHeader("direct:in", "Four", "group", "even");
        template.sendBodyAndHeader("direct:in", "Five", "group", "odd");
        template.sendBodyAndHeader("direct:in", "Six", "group", "even");
        template.sendBodyAndHeader("direct:in", "Seven", "group", "odd");
        template.sendBodyAndHeader("direct:in", "Eight", "group", "even");
        template.sendBodyAndHeader("direct:in", "Nine", "group", "odd");
        template.sendBodyAndHeader("direct:in", "Ten", "group", "even");

        assertMockEndpointsSatisfied();

        List<Exchange> receivedExchanges = mockOut.getReceivedExchanges();
        @SuppressWarnings("unchecked")

        Set<String> odd = Collections.checkedSet(receivedExchanges.get(0).getIn().getBody(Set.class), String.class);
        assertTrue(odd.containsAll(Arrays.asList("One", "Three", "Five", "Seven", "Nine")));
        //5 messages per set due to completion size = 5

        @SuppressWarnings("unchecked")
        Set<String> even = Collections.checkedSet(receivedExchanges.get(1).getIn().getBody(Set.class), String.class);
        assertTrue(even.containsAll(Arrays.asList("Two", "Four", "Six", "Eight", "Ten")));
        //5 messages per set due to completion size = 5
    }


    @Test
    public void testAggregationCorrelation() throws InterruptedException {

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(1);

        template.sendBodyAndHeader("direct:aggr", "One", "group", "number");
        template.sendBodyAndHeader("direct:aggr", "Two", "group", "number");
        template.sendBodyAndHeader("direct:aggr", "Three", "group", "number");
        template.sendBodyAndHeader("direct:aggr", "Four", "group", "number");
        template.sendBodyAndHeader("direct:aggr", "Five", "group", "number");
        template.sendBodyAndHeader("direct:aggr", "Six", "group", "number");
        template.sendBodyAndHeader("direct:aggr", "Seven", "group", "number");
        template.sendBodyAndHeader("direct:aggr", "Eight", "group", "number");
        template.sendBodyAndHeader("direct:aggr", "Nine", "group", "number");
        template.sendBodyAndHeader("direct:aggr", "Ten", "group", "number");

        assertMockEndpointsSatisfied();

        List<Exchange> receivedExchanges = mockOut.getReceivedExchanges();
        @SuppressWarnings("unchecked")
        Set<String> number = Collections.checkedSet(receivedExchanges.get(0).getIn().getBody(Set.class), String.class);
        assertTrue(number.containsAll(Arrays.asList("One", "Three", "Five", "Seven", "Two", "Four", "Six", "Eight",
                "Nine", "Ten")));
        //10 messages per set due to completion time  = 5s
    }
}