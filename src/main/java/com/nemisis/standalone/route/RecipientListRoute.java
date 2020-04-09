package com.nemisis.standalone.route;

import org.apache.camel.builder.RouteBuilder;

/**
 * Simple RecipientList example.
 *
 * When you want to dynamically (at runtime) decide a list of endpoints that an individual message should be sent to,
 * use the Recipient List EIP. This EIP is made up of two phases: deciding where to route the message, and
 * subsequently invoking those route steps. It can be thought of as a dynamic Multicast, and behaves in much the same
 * way.
 *
 */
public class RecipientListRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start")
            .setHeader("endpointsToBeTriggered")
                .method(MessageRouter.class, "getEndpointsToRouteMessageTo")
            .recipientList(header("endpointsToBeTriggered"));

        from("direct:order.priority").to("mock:order.priority");
        from("direct:order.normal").to("mock:order.normal");
        from("direct:billing").to("mock:billing");
        from("direct:unrecognized").to("mock:unrecognized");
    }
}