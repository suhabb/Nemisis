package com.nemisis.standalone.route;

import org.apache.camel.builder.RouteBuilder;

public class DynamicRouterRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:start")
            .dynamicRouter(method(MyDynamicRouter.class, "routeMe"));//class name , method name - routeMe in class

        from("direct:other")
            .to("mock:other");
    }
}