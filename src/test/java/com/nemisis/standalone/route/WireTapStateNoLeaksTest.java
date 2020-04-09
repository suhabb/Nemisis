package com.nemisis.standalone.route;

import com.nemisis.standalone.model.Cheese;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.camel.language.simple.SimpleLanguage.simple;

public class WireTapStateNoLeaksTest extends CamelTestSupport {
    private static final Logger LOG = LoggerFactory.getLogger(WireTapStateNoLeaksTest.class);

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @EndpointInject(uri = "mock:tapped")
    private MockEndpoint tapped;

    @EndpointInject(uri = "mock:out")
    private MockEndpoint out;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new WireTapStateNoLeaksRoute();
    }

    @Test
    public void testOutMessageUnaffectedByTappedRoute() throws InterruptedException {
        final Cheese cheese = new Cheese();
        cheese.setAge(1);

        // should receive same object that was sent
        out.expectedBodiesReceived(cheese);
        out.setExpectedMessageCount(1);
        // since copy was sent to wire tap, age should remain unchanged
        out.message(0).body().isEqualTo(cheese);
        out.message(0).expression(simple("${body.age} == 1"));

        tapped.setExpectedMessageCount(1);
        tapped.message(0).expression(simple("${body.age} == 2"));
        tapped.setResultWaitTime(1000);

        template.sendBody(cheese);

        assertMockEndpointsSatisfied();

        final Cheese outCheese = out.getReceivedExchanges().get(0).getIn().getBody(Cheese.class);
        final Cheese tappedCheese = tapped.getReceivedExchanges().get(0).getIn().getBody(Cheese.class);

        LOG.info("cheese = {}; out = {}; tapped = {}", cheese, outCheese, tappedCheese);

        LOG.info("cheese == out = {}", (cheese == outCheese));
        LOG.info("cheese == tapped = {}", (cheese == tappedCheese));
        LOG.info("out == tapped = {}", (outCheese == tappedCheese));

        assertNotSame(outCheese, tappedCheese);
        assertSame(outCheese, cheese);
    }
}