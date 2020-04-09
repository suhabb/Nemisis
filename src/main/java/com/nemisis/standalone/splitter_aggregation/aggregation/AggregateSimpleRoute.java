package com.nemisis.standalone.splitter_aggregation.aggregation;

import org.apache.camel.builder.RouteBuilder;

public class AggregateSimpleRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        /*
        Within the block, the correlationExpression element is used to define the value that will be used to
        aggregate messages. Exchanges that evaluate to the same expression result will be aggregated.
        The correlation expression appears as a nested element within the aggregate block.
        */

       /*
        A Predicate to indicate when an aggregated exchange is complete. If this is not specified and the
        AggregationStrategy
        object implements Predicate, the aggregationStrategy object will be used as the completionPredicate.
        */

        from("direct:in")
            .log("${threadName} - ${body}")
            .aggregate(header("group"), new SetAggregationStrategy()).completionSize(5)// group - maintains a diff between odd and even
                .log("${threadName} - out")
                .to("mock:out")
            .end();

        from("direct:aggr")
                .log("${threadName} - ${body}")
                .aggregate(header("group"), new SetAggregationStrategy()).completionInterval(5000)// group - maintains a diff between odd and even
                .log("${threadName} - out")
                .to("mock:out")
                .end();



    }
}