package com.nemisis.standalone.splitter_aggregation.aggregation;

import org.apache.camel.builder.RouteBuilder;

public class AggregateDynamicCompletionSizeRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:in")
            .log("${threadName} - ${body}")
            .aggregate(header("group"), new SetAggregationStrategy())
                    .completionSize(header("batchSize"))
                .log("${threadName} - out")
                .to("mock:out")
            .end();
    }
}