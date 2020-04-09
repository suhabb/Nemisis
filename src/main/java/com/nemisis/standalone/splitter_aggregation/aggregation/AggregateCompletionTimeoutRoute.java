package com.nemisis.standalone.splitter_aggregation.aggregation;

import org.apache.camel.builder.RouteBuilder;


public class AggregateCompletionTimeoutRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:in")
            .log("Aggregation : ${threadName} - ${body}")
            .aggregate(header("group"), new SetAggregationStrategy())
                    .completionSize(10).completionTimeout(1000)
                .log("${threadName} - out")
                .to("mock:out")
            .end();
    }
}