package com.nemisis.standalone.route;

/**
 * Simple multicast example with parallel processing.
 *
 * When a message hits the Multicast EIP, a shallow copy of the message (see the Wire
 * Tap – sending a copy of the message elsewhere recipe for an explanation) is made for
 * each step specified in the EIP definition.
 *
 * The thread currently processing the message triggers each of the specified steps one by one with a unique copy of
 * the message. Any changes made to these copied messages will
 * not be visible in the original message that continues flowing down the main route once the Multicast is complete.
 */
import com.nemisis.standalone.stratergy.ConcatenatingAggregationStrategy;
import org.apache.camel.builder.RouteBuilder;

public class MulticastWithAggregationRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start")
            .multicast().aggregationStrategy(new ConcatenatingAggregationStrategy())
                .to("direct:first")
                .to("direct:second")
            .end()
            .transform(body()); // copy the In message to the Out message; this will become the route response

        from("direct:first")
            .transform(constant("first response"));

        from("direct:second")
            .transform(constant("second response"));
    }
}