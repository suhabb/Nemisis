package com.nemisis.standalone.splitter_aggregation.split_join.aggregate;

import com.nemisis.standalone.splitter_aggregation.aggregation.AggregateParallelProcessingRoute;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;


public class AggregateParallelProcessingTest extends CamelTestSupport {

    @Override
    public RouteBuilder createRouteBuilder() {
        return new AggregateParallelProcessingRoute();
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

        final List<Exchange> receivedExchanges = mockOut.getReceivedExchanges();
        final Message message1 = receivedExchanges.get(0).getIn();
        final Message message2 = receivedExchanges.get(1).getIn();

        log.info("exchange(0).header.group = {}", message1.getHeader("group"));
        log.info("exchange(0).property.CamelAggregatedCompletedBy = {}", message1.getExchange()
                .getProperty("CamelAggregatedCompletedBy"));
        log.info("exchange(1).header.group = {}", message2.getHeader("group"));
        log.info("exchange(1).property.CamelAggregatedCompletedBy = {}", message2.getExchange()
                .getProperty("CamelAggregatedCompletedBy"));

        final List<String> odd = Arrays.asList("One", "Three", "Five", "Seven", "Nine");
        final List<String> even = Arrays.asList("Two", "Four", "Six", "Eight", "Ten");

        @SuppressWarnings("unchecked")
        final Set<String> set1 = Collections.checkedSet(message1.getBody(Set.class), String.class);
        @SuppressWarnings("unchecked")
        final Set<String> set2 = Collections.checkedSet(message2.getBody(Set.class), String.class);

        if ("odd".equals(message1.getHeader("group"))) {
            assertTrue(set1.containsAll(odd));
            assertTrue(set2.containsAll(even));
        } else {
            assertTrue(set1.containsAll(even));
            assertTrue(set2.containsAll(odd));
        }
    }
}