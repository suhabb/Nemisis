package com.nemisis.standalone.route;

import com.nemisis.standalone.model.CheeseCloningProcessor;
import com.nemisis.standalone.model.CheeseRipener;
import org.apache.camel.builder.RouteBuilder;

/**
 * Route showing wiretap without state leakage.
 * When you want to process the current message in the background (concurrently) to the main route,
 * without requiring a response, the Wire Tap EIP can help. A typical use case for this
 * is logging the message to a backend system. The main thread of execution will continue to process
 * the message through the current route as usual, while Wire Tap allows additional messaging processing to
 * occur outside of the main route.
 */
public class WireTapStateNoLeaksRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start")
            .log("Cheese is ${body.age} months old")
            .wireTap("direct:processInBackground")//send it to background
                .onPrepare(new CheeseCloningProcessor())
            .delay(constant(1000))
            .to("mock:out");

        from("direct:processInBackground")
            .bean(CheeseRipener.class, "ripen")
            .to("mock:tapped");
    }
}