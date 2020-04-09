package com.nemisis.standalone.transformation.enrich;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class EnrichWithAggregatorTest extends CamelTestSupport {

    @Override
    protected RouteBuilder[] createRouteBuilders() throws Exception {

        EnrichWithAggregatorRoute routeBuilder = new EnrichWithAggregatorRoute();
        routeBuilder.setMergeInReplacementText(context().getRegistry().lookupByNameAndType("mergeInReplacementText", MergeInReplacementText.class));

        return new RouteBuilder[]{routeBuilder, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:expander")
                    .bean(AbbreviationExpander.class, "expand");
            }
        }};
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndiRegistry = super.createRegistry();

        // register beanId for use by EnrichRoute
        // you could also use Spring or Blueprint 'bean' to create and configure
        // these references where the '<bean id="<ref id>">'
        jndiRegistry.bind("mergeInReplacementText", new MergeInReplacementText());

        return jndiRegistry;
    }

    @Test
    public void testEnrichWithAggregator() throws Exception {
        String response = template.requestBody("direct:start", "Hello MA", String.class);

        assertEquals("Hello Massachusetts", response);

        response = template.requestBody("direct:start", "I like CA", String.class);

        assertEquals("I like California", response);
    }
}