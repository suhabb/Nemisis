package com.nemisis.standalone.splitter_aggregation.split_join;

import com.nemisis.standalone.splitter_aggregation.SplitInGroup;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Demonstrates the splitting of large fiels in groups.
 */
public class SplitInGroupTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new SplitInGroup();
    }

    @Test
    public void testSplitInGroup() throws Exception {

        String body = "The group option is a number, that must be a, positive number, that dictates how, " +
                "many groups to " +
                "combine together,. Each part will be, combined using the token.\n" +
                "\n" +
                "So in the example above the ,message being sent ,to the activemq " +
                "order queue, will contain, lines, and each, line separated ,by the token";

        MockEndpoint mockSplit = getMockEndpoint("mock:split");

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.expectedMessageCount(5);

        template.sendBody("direct:inbox", body);

        assertMockEndpointsSatisfied();
    }
}
