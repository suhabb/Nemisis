package com.nemisis.standalone.splitter_aggregation.split_join;

import com.nemisis.standalone.splitter_aggregation.SplitExceptionHandlingRoute;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SplitExceptionHandlingTest extends CamelTestSupport {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new SplitExceptionHandlingRoute();
    }

    @Test
    public void testRemainderElementsProcessedOnException() throws Exception {
        String[] array = new String[]{"one", "two", "three"};

        MockEndpoint mockSplit = getMockEndpoint("mock:split");
        mockSplit.expectedMessageCount(2);
        mockSplit.expectedBodiesReceived("two", "three");

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.expectedMessageCount(0);

        try {
            template.sendBody("direct:in", array);
            fail("Exception not thrown");
        } catch (CamelExecutionException ex) {
            assertTrue(ex.getCause() instanceof IllegalStateException);
            assertMockEndpointsSatisfied();
        }
    }
}