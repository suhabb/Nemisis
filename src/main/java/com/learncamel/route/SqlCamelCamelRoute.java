package com.learncamel.route;

import com.learncamel.alert.MailProcessor;
import com.learncamel.domain.Item;
import com.learncamel.exception.DataException;
import com.learncamel.process.BuildSQLProcessor;
import com.learncamel.process.SuccessProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;


@Component
@Slf4j
public class SqlCamelCamelRoute extends RouteBuilder{


    @Autowired
    Environment environment;

    @Qualifier("dataSource")
    @Autowired
    DataSource dataSource;

    @Autowired
    BuildSQLProcessor buildSQLProcessor;

    @Autowired
    SuccessProcessor successProcessor;

    @Autowired
    MailProcessor mailProcessor;


    @Override
    public void configure() throws Exception {

        DataFormat bindy = new BindyCsvDataFormat(Item.class);

        errorHandler(deadLetterChannel("log:errorInRoute?level=ERROR&showProperties=true")
                .maximumRedeliveries(3).redeliveryDelay(3000).backOffMultiplier(2).retryAttemptedLogLevel(LoggingLevel.ERROR));

        onException(PSQLException.class).log(LoggingLevel.ERROR,"PSQLException in the route ${body}")
                .maximumRedeliveries(3).redeliveryDelay(3000).backOffMultiplier(2).retryAttemptedLogLevel(LoggingLevel.ERROR);

        onException(DataException.class).log(LoggingLevel.ERROR, "DataException in the route ${body}").process(mailProcessor);


        from("{{startRoute}}").routeId("mainRoute")
                .log("Timer Invoked and the body" + environment.getProperty("message"))
                .choice()
                    .when((header("env").isNotEqualTo("mock")))
                        .pollEnrich("{{fromRoute}}")
                    .otherwise()
                        .log("mock env flow and the body is ${body}")
                .end()
                .to("{{toRoute1}}")
                .unmarshal(bindy)
                .log("The unmarshaled object is ${body}")
                .split(body())
                    .process(buildSQLProcessor)
                    .to("{{toRoute2}}")
                .end()
        .process(successProcessor)
        .to("{{toRoute3}}");



    }
}
