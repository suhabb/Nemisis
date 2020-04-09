package com.nemisis.standalone.route;

import org.apache.camel.builder.RouteBuilder;


/*
* When you need to limit the number of messages flowing through a route during a specified time period,
* the Throttler EIP can help. For example, if you have a downstream system that can only handle 10 requests per second,
* using a Throttler EIP within your route can ensure that you do not exceed that rate.
*
* */
public class ThrottlerDynamicRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start")
            .to("mock:unthrottled")
            .throttle(header("ThrottleRate")).timePeriodMillis(10000)//Throttle rate = 3 request for 10s
                .to("mock:throttled")
            .end()
            .to("mock:after");
    }
}