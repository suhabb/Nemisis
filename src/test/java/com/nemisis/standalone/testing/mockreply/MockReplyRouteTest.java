

package com.nemisis.standalone.testing.mockreply;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.SimpleBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.language.constant.ConstantLanguage;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class MockReplyRouteTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:replying")
    private MockEndpoint mockReplying;

    @EndpointInject(uri = "mock:out")
    private MockEndpoint mockOut;

    @Produce(uri = "direct:in")
    ProducerTemplate in;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new MockReplyRoute();
    }

    @Test
    public void testReplyingFromMockByExpression() throws InterruptedException {

        mockReplying.returnReplyBody(SimpleBuilder.simple("Hello ${body}"));
        mockOut.expectedBodiesReceived("Hello Camel");

        in.sendBody("Camel");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testReplyingWithHeaderFromMockByExpression() throws InterruptedException {

        mockReplying.returnReplyHeader("responder", ConstantLanguage.constant("fakeService"));
        mockOut.message(0).header("responder").isEqualTo("fakeService");

        in.sendBody("Camel");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testReplyingFromMockByProcessor() throws InterruptedException {

        mockReplying.whenAnyExchangeReceived(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                Message in = exchange.getIn();
                in.setBody("Hey " + in.getBody());
            }
        });

        // the 1st exchange will be handled by a different Processor
        mockReplying.whenExchangeReceived(1, new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                Message in = exchange.getIn();
                in.setBody("Czesc " + in.getBody()); // Polish
            }
        });

        mockOut.expectedBodiesReceived("Czesc Camel", "Hey Camel", "Hey Camel");

        in.sendBody("Camel");
        in.sendBody("Camel");
        in.sendBody("Camel");

        assertMockEndpointsSatisfied();
    }
}
