package com.nemisis.standalone.splitter_aggregation.split_join.aggregate;

import com.nemisis.standalone.splitter_aggregation.aggregation.AggregateDynamicCompletionSizeRoute;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Test class that demonstrates a use of a dynamic completion size with aggregation.
 */
public class AggregateDynamicCompletionSizeTest extends CamelTestSupport {

    @Override
    public RouteBuilder createRouteBuilder() {
        return new AggregateDynamicCompletionSizeRoute();
    }

    @Test
    public void testAggregation() throws InterruptedException {
        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(2);

        Map<String, Object> oddHeaders = new HashMap<String, Object>();
        oddHeaders.put("group", "odd");
        oddHeaders.put("batchSize", "5");

        Map<String, Object> evenHeaders = new HashMap<String, Object>();
        evenHeaders.put("group", "even");
        evenHeaders.put("batchSize", "4");

        template.sendBodyAndHeaders("direct:in", "One", oddHeaders);
        template.sendBodyAndHeaders("direct:in", "Two", evenHeaders);
        template.sendBodyAndHeaders("direct:in", "Three", oddHeaders);
        template.sendBodyAndHeaders("direct:in", "Four", evenHeaders);
        template.sendBodyAndHeaders("direct:in", "Five", oddHeaders);
        template.sendBodyAndHeaders("direct:in", "Six", evenHeaders);
        template.sendBodyAndHeaders("direct:in", "Seven", oddHeaders);
        template.sendBodyAndHeaders("direct:in", "Eight", evenHeaders);
        template.sendBodyAndHeaders("direct:in", "Nine", oddHeaders);

        assertMockEndpointsSatisfied();

        List<Exchange> receivedExchanges = mockOut.getReceivedExchanges();
        @SuppressWarnings("unchecked")
        Set<String> even = Collections.checkedSet(receivedExchanges.get(0).getIn().getBody(Set.class), String.class);
        assertTrue(even.containsAll(Arrays.asList("Two", "Four", "Six", "Eight")));

        @SuppressWarnings("unchecked")
        Set<String> odd = Collections.checkedSet(receivedExchanges.get(1).getIn().getBody(Set.class), String.class);
        assertTrue(odd.containsAll(Arrays.asList("One", "Three", "Five", "Seven", "Nine")));
    }
}