package com.nemisis.standalone.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class FilteringTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new FilteringRoute();
    }

    @Test
    public void testFirstFilter() throws Exception {

        final MockEndpoint mockEndpointC = getMockEndpoint("mock:C");
        mockEndpointC.expectedMessageCount(1);
        mockEndpointC.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);

        final MockEndpoint mockEndpointAfterC = getMockEndpoint("mock:afterC");
        mockEndpointAfterC.expectedMessageCount(1);

        // FILTER_MATCHED set to true if message matched previous Filter Predicate
        mockEndpointAfterC.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);

        getMockEndpoint("mock:amel").expectedMessageCount(0);

        final MockEndpoint mockEndpointOther = getMockEndpoint("mock:other");
        mockEndpointOther.expectedMessageCount(1);

        // FILTER_MATCHED set to true if message matched previous Filter Predicate
        mockEndpointOther.expectedPropertyReceived(Exchange.FILTER_MATCHED, false);

        template.sendBody("direct:start", "Cooks Rocks");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testSecondFilter() throws Exception {
        getMockEndpoint("mock:C").expectedMessageCount(0);

        final MockEndpoint mockEndpointAfterC = getMockEndpoint("mock:afterC");
        mockEndpointAfterC.expectedMessageCount(1);

        // FILTER_MATCHED set to true if message matched previous Filter Predicate
        mockEndpointAfterC.expectedPropertyReceived(Exchange.FILTER_MATCHED, false);

        final MockEndpoint mockEndpointAmel = getMockEndpoint("mock:amel");
        mockEndpointAmel.expectedMessageCount(1);
        mockEndpointAmel.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);

        final MockEndpoint mockEndpointOther = getMockEndpoint("mock:other");
        mockEndpointOther.expectedMessageCount(1);

        // FILTER_MATCHED set to true if message matched previous Filter Predicate
        mockEndpointOther.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);

        template.sendBody("direct:start", "amel is in Belgium");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testBothFilter() throws Exception {
        final MockEndpoint mockEndpointC = getMockEndpoint("mock:C");
        mockEndpointC.expectedMessageCount(1);
        mockEndpointC.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);

        final MockEndpoint mockEndpointAfterC = getMockEndpoint("mock:afterC");
        mockEndpointAfterC.expectedMessageCount(1);

        // FILTER_MATCHED set to true if message matched previous Filter Predicate
        mockEndpointAfterC.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);

        final MockEndpoint mockEndpointAmel = getMockEndpoint("mock:amel");
        mockEndpointAmel.expectedMessageCount(1);
        mockEndpointAmel.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);

        final MockEndpoint mockEndpointOther = getMockEndpoint("mock:other");
        mockEndpointOther.expectedMessageCount(1);

        // FILTER_MATCHED set to true if message matched previous Filter Predicate
        mockEndpointOther.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);

        template.sendBody("direct:start", "Camel Rocks!");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testOther() throws Exception {
        getMockEndpoint("mock:C").expectedMessageCount(0);

        final MockEndpoint mockEndpointAfterC = getMockEndpoint("mock:afterC");
        mockEndpointAfterC.expectedMessageCount(1);

        // FILTER_MATCHED set to true if message matched previous Filter Predicate
        mockEndpointAfterC.expectedPropertyReceived(Exchange.FILTER_MATCHED, false);

        getMockEndpoint("mock:amel").expectedMessageCount(0);

        final MockEndpoint mockEndpointOther = getMockEndpoint("mock:other");
        mockEndpointOther.expectedMessageCount(1);

        // FILTER_MATCHED set to true if message matched previous Filter Predicate
        mockEndpointOther.expectedPropertyReceived(Exchange.FILTER_MATCHED, false);

        template.sendBody("direct:start", "Hello World");

        assertMockEndpointsSatisfied();
    }
}