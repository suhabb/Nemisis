package com.nemisis.standalone.transformation;

import com.nemisis.standalone.model.View;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class JsonTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new JsonRoute();
    }

    @Test
    public void testJsonMarshal() throws Exception {
        View view = new View();

        view.setAge(29);
        view.setHeight(46);
        view.setWeight(34);

        String response = template.requestBody("direct:marshal", view, String.class);

        log.info(response);
        assertEquals("{\"com.nemisis.standalone.model.View\":{\"age\":29,\"weight\":34,\"height\":46}}", response);
    }

    @Test
    public void testJsonUnmarshal() throws Exception {
        final String request = "{\"com.nemisis.standalone.model.View\":{\"age\":29,\"weight\":34,\"height\":46}}";

        View response = template.requestBody("direct:unmarshal", request, View.class);

        View view = new View();

        view.setAge(29);
        view.setHeight(46);
        view.setWeight(34);

        assertEquals(view, response);
    }
}
