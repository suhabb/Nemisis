package com.nemisis.standalone.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class MyProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String result = "Unknown language";

        final Message inMessage = exchange.getIn();
        final String body = inMessage.getBody(String.class);
        final String language = inMessage.getHeader("language", String.class);

        if ("en".equals(language)) {
            result = "Hello " + body;
        } else if ("fr".equals(language)) {
            result = "Bonjour " + body;
        }

        inMessage.setBody(result);
    }
}