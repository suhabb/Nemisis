package com.nemisis.standalone.main;

import com.nemisis.standalone.route.LogMessageOnTimerEventRouteBuilder;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;


public class LogMessageOnTimerEventRouteMain {

    public static void main(String[] args) {
        CamelContext context = new DefaultCamelContext();
        try {
            context.addRoutes(
                    new LogMessageOnTimerEventRouteBuilder());
            // Start the context. This loads the route definitions that you have added, and processes messages through them in the background:
            context.start();
            Thread.sleep(3000);
            //  When the Camel application is ready to be shut down, call:
            context.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
