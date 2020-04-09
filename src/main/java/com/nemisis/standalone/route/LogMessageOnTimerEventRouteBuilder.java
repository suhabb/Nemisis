package com.nemisis.standalone.route;

import org.apache.camel.builder.RouteBuilder;

public class LogMessageOnTimerEventRouteBuilder extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        //   The from(...) statement at the start of a route defines an endpoint,
        //   or a technology-specific location, that the Camel routing engine uses to fetch messages.
        //   Endpoints are defined using URIs, such as in the preceding example, timer:logMessageTimer.
        //   The first part of the URI specifies the component that is being used to consume the message,
        //   and the remaining is a set of instructions for that specific component.
        from("timer:logMessageTimer?period=1s")
                .log("Event triggered by ${property.CamelTimerName}"
                        + " at ${header.CamelTimerFiredTime}");
    }

}
