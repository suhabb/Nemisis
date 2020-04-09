package com.nemisis.standalone.splitter_aggregation.aggregation;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
    The AggregationStrategy interface defines a single method:
    Exchange aggregate(Exchange oldExchange, Exchange newExchange);
    It receives two Exchange objects as parameters, and returns a single Exchange object that represents the
    merged result.
    When it is called for the first time, the AggregationStrategy will receive a null for the oldExchange parameter,
    and, as such, needs to be able to deal with this condition. On subsequent invocations, the oldExchange parameter
    will contain the previously aggregated exchange.
*/
@Slf4j
public class SetAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        String body = newExchange.getIn().getBody(String.class);
        log.info("Body of new exchange : {}",body);
        //    When it is called for the first time, the AggregationStrategy will receive a null for the
        //    oldExchange parameter,
        if (oldExchange == null) {
            Set<String> set = new HashSet<String>();
            set.add(body);
            newExchange.getIn().setBody(set);
            log.info("Old Exchange : {}",body);
            return newExchange;
        } else {//On subsequent invocations, the oldExchange parameter will contain the previously aggregated exchange.
            @SuppressWarnings("unchecked")
            Set<String> set = Collections.checkedSet(oldExchange.getIn().getBody(Set.class), String.class);
            set.add(body);
            set.forEach(s-> {
                log.info("New exchange : Aggregate body of set : {}",s);
                    }
            );
            return oldExchange;
        }
    }
}