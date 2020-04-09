package com.nemisis.standalone.route;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
/*
The newExchange parameter is the current Multicast response being processed, and oldExchange parameter is the
merged result so far. Note that the first time the aggregate() method is called, oldExchange will be null
*/

public class MulticastWithAggregationTest extends CamelTestSupport {

    public static final String MESSAGE_BODY = "Message to be multicast";

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new MulticastWithAggregationRoute();
    }

    @Test
    public void testAggregationOfResponsesFromMulticast() throws InterruptedException {
        String response = (String) template.requestBody(MESSAGE_BODY);
        assertEquals("first response,second response", response);
    }

}