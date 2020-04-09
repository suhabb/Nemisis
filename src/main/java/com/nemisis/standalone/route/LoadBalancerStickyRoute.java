package com.nemisis.standalone.route;

import org.apache.camel.builder.RouteBuilder;

public class LoadBalancerStickyRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start")
            .loadBalance()
                .sticky(header("customerId"))
                    .to("mock:first")
                    .to("mock:second")
                    .to("mock:third")
            .end()
            .to("mock:out");
    }
}