package com.nemisis.standalone.splitter_aggregation.split_join;

import com.nemisis.standalone.splitter_aggregation.ListWrapper;
import com.nemisis.standalone.splitter_aggregation.SplitSimpleExpressionRoute;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Demonstrates the splitting of a List by using a Simple expression to locate it in an object graph.
 */
public class SplitSimpleExpressionTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new SplitSimpleExpressionRoute();
    }

    @Test
    public void testSimpleExpressionReferenceToList() throws Exception {

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.expectedMessageCount(3);
        mockOut.expectedBodiesReceived("one", "two", "three");

        List<String> list = new LinkedList<String>();
        list.add("one");
        list.add("two");
        list.add("three");
        ListWrapper wrapper = new ListWrapper();
        wrapper.setWrapped(list);

        template.sendBody("direct:in", wrapper);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void givenTestWrapperName() throws Exception{

        MockEndpoint mockOutEndpoint = getMockEndpoint("mock:out");
        mockOutEndpoint.expectedMessageCount(1);
        mockOutEndpoint.expectedBodiesReceived("Tea");

        ListWrapper list= new ListWrapper();
        list.setWrapperName("Tea");

        template.sendBody("direct:wrapper",list);
        assertMockEndpointsSatisfied();

    }

}