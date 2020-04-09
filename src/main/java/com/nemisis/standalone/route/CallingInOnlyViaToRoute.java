package com.nemisis.standalone.route;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

/**
 * Changing the MEP of a message for one endpoint invocation to InOnly.
 *
 * When processing a message in a Request-Response (InOut) route, you sometimes need to send the message to an endpoint,
 * but do not want to receive a response. This recipe shows you how to invoke such an endpoint using the InOnly MEP.
 * The MEP used can radically alter the behavior of an endpoint. For example, if you invoke a JMS endpoint within a
 * request-response (InOut) route, it will send a message to a queue and set up a listener on a temporary destination
 * waiting for a response; this is known as request- response over messaging.
 */
public class CallingInOnlyViaToRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:start")
            .to("mock:beforeOneWay")
            .to(ExchangePattern.InOnly, "direct:oneWay")
            .to("mock:afterOneWay")
            .transform().constant("Done");

        from("direct:oneWay")
            .to("mock:oneWay");
    }
}