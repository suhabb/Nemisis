

package com.nemisis.standalone.testing.dataset;


import com.nemisis.standalone.testing.java.SlowlyTransformingRoute;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Test class that demonstrates using the DataSet component to load test a route.
 */
public class SlowlyTransformingRouteLoadTest extends CamelTestSupport {

    @Override
    protected CamelContext createCamelContext() throws Exception {
        final int testBatchSize = 1000;

        InputDataSet inputDataSet = new InputDataSet();
        inputDataSet.setSize(testBatchSize);

        ExpectedOutputDataSet expectedOutputDataSet = new ExpectedOutputDataSet();
        expectedOutputDataSet.setSize(testBatchSize);

        SimpleRegistry registry = new SimpleRegistry();
        registry.put("input", inputDataSet);
        registry.put("expectedOutput", expectedOutputDataSet);

        return new DefaultCamelContext(registry);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {

        SlowlyTransformingRoute routeBuilder = new SlowlyTransformingRoute();
        routeBuilder.setSourceUri("dataset:input?produceDelay=-1");
        routeBuilder.setTargetUri("dataset:expectedOutput");
        return routeBuilder;
    }

    @Test
    public void testPayloadsTransformedInExpectedTime() throws InterruptedException {

        // A DataSetEndpoint is a sub-class of MockEndpoint that sets up expectations based on
        // the messages created, and the size property on the object.
        // All that is needed for us to test this route is to assert that the endpoint was satisfied.
        MockEndpoint expectedOutput = getMockEndpoint("dataset:expectedOutput");
        expectedOutput.setResultWaitTime(10000);
        expectedOutput.assertIsSatisfied();
    }
}
