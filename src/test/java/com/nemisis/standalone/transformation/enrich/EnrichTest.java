package com.nemisis.standalone.transformation.enrich;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class EnrichTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new EnrichRoute();
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndiRegistry = super.createRegistry();

        // register beanId for use by EnrichRoute
        // you could also use Spring or Blueprint 'bean' to create and configure
        // these references where the '<bean id="<ref id>">'
        jndiRegistry.bind("myExpander", new AbbreviationExpander());

        return jndiRegistry;
    }

    @Test
    public void testEnrich() throws Exception {
        String response = template.requestBody("direct:start", "MA", String.class);

        assertEquals("Massachusetts", response);

        response = template.requestBody("direct:start", "CA", String.class);

        assertEquals("California", response);
    }
}