package com.nemisis.standalone.error_handling.retry;

import com.nemisis.standalone.error_handling.shared.SporadicProcessor;
import org.apache.camel.builder.RouteBuilder;

public class RetryRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        errorHandler(defaultErrorHandler().maximumRedeliveries(2));

        from("direct:start")
            .bean(SporadicProcessor.class)
            .to("mock:result");

        from("direct:routeSpecific")
            .errorHandler(defaultErrorHandler().maximumRedeliveries(2))
            .bean(SporadicProcessor.class)
            .to("mock:result");

        from("direct:routeSpecificDelay")
            .errorHandler(defaultErrorHandler().maximumRedeliveries(2).redeliveryDelay(500))
            .bean(SporadicProcessor.class)
            .to("mock:result");
    }
}
