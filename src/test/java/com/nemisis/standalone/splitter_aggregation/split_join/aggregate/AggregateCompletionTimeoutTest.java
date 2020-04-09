package com.nemisis.standalone.splitter_aggregation.split_join.aggregate;

import com.nemisis.standalone.splitter_aggregation.aggregation.AggregateCompletionTimeoutRoute;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

/**
 * Test class that demonstrates a aggregation using timeouts.
 */
public class AggregateCompletionTimeoutTest extends CamelTestSupport {

    @Override
    public RouteBuilder createRouteBuilder() {

        return new AggregateCompletionTimeoutRoute();
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

        for (Exchange exchange : mockOut.getReceivedExchanges()) {
            @SuppressWarnings("unchecked")
            Set<String> set = Collections.checkedSet(exchange.getIn().getBody(Set.class), String.class);
            if (set.contains("One")) { // odd
                assertTrue(set.containsAll(Arrays.asList("One", "Three", "Five", "Seven", "Nine")));
            } else { // even
                assertTrue(set.containsAll(Arrays.asList("Two", "Four", "Six", "Eight", "Ten")));
            }
        }
    }
}