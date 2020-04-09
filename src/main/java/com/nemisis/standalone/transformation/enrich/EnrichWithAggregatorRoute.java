package com.nemisis.standalone.transformation.enrich;

import org.apache.camel.builder.RouteBuilder;


/*

When using the Enricher EIP, it is a common requirement to include a processing step
before the enrich call to set up the message body to an appropriate request format for the enriching endpoint.
It is also likely that you will need to define an additional processing step to merge the results of the endpoint
invocation with your original exchange. This recipe will show you, through a simple example, how to perform these
setup and enrichment steps when using the Enricher EIP.
*/

public class EnrichWithAggregatorRoute extends RouteBuilder {

    private MergeInReplacementText mergeInReplacementText;

    @Override
    public void configure() throws Exception {

        from("direct:start")
            .bean(mergeInReplacementText, "setup")
            .enrich("direct:expander", mergeInReplacementText);
    }

    public MergeInReplacementText getMergeInReplacementText() {
        return mergeInReplacementText;
    }

    public void setMergeInReplacementText(MergeInReplacementText myMerger) {
        this.mergeInReplacementText = myMerger;
    }
}