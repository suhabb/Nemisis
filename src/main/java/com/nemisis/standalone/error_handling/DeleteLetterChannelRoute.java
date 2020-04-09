/*
 * Copyright (C) Scott Cranton, Jakub Korab, and Christian Posta
 * https://github.com/CamelCookbook
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nemisis.standalone.error_handling;

import com.nemisis.standalone.error_handling.shared.FlakyProcessor;
import org.apache.camel.builder.RouteBuilder;

/*
* Camel's Dead Letter Channel error handler helps when you want to send generic errors to a particular endpoint.
*  It can also be useful if you just want to capture the message that caused the error for manual processing later.
*
* */
public class DeleteLetterChannelRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        errorHandler(deadLetterChannel("seda:error"));

        from("direct:start")
                .routeId("myDlcRoute")
            .setHeader("myHeader").constant("changed")
            .bean(FlakyProcessor.class)
            .to("mock:result");

        from("direct:multiroute")
                .routeId("myDlcMultistepRoute")
            .setHeader("myHeader").constant("multistep")
            .inOut("seda:flakyroute")
            .setHeader("myHeader").constant("changed")
            .to("mock:result");

        from("seda:flakyroute")
                .routeId("myFlakyRoute")
            .setHeader("myHeader").constant("flaky")
            .bean(FlakyProcessor.class);

        from("direct:multirouteOriginal")
                .routeId("myDlcMultistepOriginalRoute")
            .setHeader("myHeader").constant("multistep")
            .inOut("seda:flakyrouteOriginal")
            .setHeader("myHeader").constant("changed")
            .to("mock:result");

        from("seda:flakyrouteOriginal")
                .routeId("myFlakyRouteOriginal")
                .errorHandler(deadLetterChannel("seda:error").useOriginalMessage())
            .setHeader("myHeader").constant("flaky")
            .bean(FlakyProcessor.class);

        from("direct:routeSpecific")
                .routeId("myDlcSpecificRoute")
                .errorHandler(deadLetterChannel("seda:error"))
            .bean(FlakyProcessor.class)
            .to("mock:result");

        from("direct:useOriginal")
                .routeId("myDlcOriginalRoute")
                .errorHandler(deadLetterChannel("seda:error").useOriginalMessage())
            .setHeader("myHeader").constant("changed")
            .bean(FlakyProcessor.class)
            .to("mock:result");

        from("seda:error")
                .routeId("myDlcChannelRoute")
            .to("log:dlc?showAll=true&multiline=true")
            .to("mock:error");
    }
}
