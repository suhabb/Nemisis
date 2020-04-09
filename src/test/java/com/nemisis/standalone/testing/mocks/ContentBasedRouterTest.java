

package com.nemisis.standalone.testing.mocks;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class ContentBasedRouterTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new ContentBasedRouterRoute();
    }

    @Test
    public void testWhen() throws Exception {
        MockEndpoint mockCamel = getMockEndpoint("mock:camel");
        mockCamel.expectedMessageCount(2);
        mockCamel.message(0).body().isEqualTo("Camel Rocks");
        mockCamel.message(0).header("verified").isEqualTo(true);
        mockCamel.message(0).arrives().noLaterThan(50).millis().beforeNext();
        mockCamel.message(0).simple("${header[verified]} == true");

        MockEndpoint mockOther = getMockEndpoint("mock:other");
        mockOther.expectedMessageCount(0);

        template.sendBody("direct:start", "Camel Rocks");
        template.sendBody("direct:start", "Loving the Camel");

        mockCamel.assertIsSatisfied();
        mockOther.assertIsSatisfied();

        Exchange exchange0 = mockCamel.assertExchangeReceived(0);
        Exchange exchange1 = mockCamel.assertExchangeReceived(1);
        assertEquals(exchange0.getIn().getHeader("verified"), exchange1.getIn().getHeader("verified"));
    }

    @Test
    public void testOther() throws Exception {
        getMockEndpoint("mock:camel").expectedMessageCount(0);
        getMockEndpoint("mock:other").expectedMessageCount(1);

        template.sendBody("direct:start", "Hello World");

        // asserts that all the mock objects involved in this test are happy
        assertMockEndpointsSatisfied();
    }
}
