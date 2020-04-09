package com.nemisis.standalone.transformation;

import com.nemisis.standalone.model.BookModel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;

//https://camel.apache.org/components/latest/dataformats/bindy-dataformat.html
public class CsvRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        final DataFormat bindy = new BindyCsvDataFormat(BookModel.class);

        from("direct:unmarshal").unmarshal(bindy);
        from("direct:marshal").marshal(bindy);
    }
}