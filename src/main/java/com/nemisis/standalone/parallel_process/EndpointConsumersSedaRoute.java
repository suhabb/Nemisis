package com.nemisis.standalone.parallel_process;

import org.apache.camel.builder.RouteBuilder;

/**
 * Route that demonstrates increasing the number of consumers on a SEDA endpoint.
 *
 * This recipe assumes that you are starting with a route that is asynchronous, for example using seda:
 * as the consumer endpoint (using the from DSL statement). SEDA is a mechanism included
 * in Camel Core for connecting routes to each other asynchronously by passing Exchange objects
 * over an in-memory queue
 *
 */
public class EndpointConsumersSedaRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("seda:in?concurrentConsumers=10")//aynchronous seda
            .delay(200)
            .log("Processing ${body}:${threadName}")
            .to("mock:out");
    }
}