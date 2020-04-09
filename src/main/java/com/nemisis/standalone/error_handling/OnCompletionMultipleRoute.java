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

import org.apache.camel.builder.RouteBuilder;

/**
 *
 * Route that demonstrates ho multiple onCompletion elements will not execute as expected -
 * only the one defined last will be used.
 */
public class OnCompletionMultipleRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        onCompletion().onCompleteOnly()
            .log("global onCompletionOnly thread: ${threadName}")
            .to("mock:globalCompleted");
        onCompletion().onFailureOnly()
            .log("global onFailureOnly thread: ${threadName}")
            .to("mock:globalFailed");

        from("direct:in")
            .onCompletion().onCompleteOnly()
                .log("onCompletionOnly triggered: ${threadName}")
                .to("mock:completed")
            .end()
            .onCompletion().onFailureOnly()
                .log("onFailureOnly thread: ${threadName}")
                .to("mock:failed")
            .end()
            .log("Original thread: ${threadName}")
            .choice()
                .when(simple("${body} contains 'explode'"))
                    .throwException(new IllegalArgumentException("Exchange caused explosion"))
            .endChoice();

        from("direct:global")
            .log("Original thread: ${threadName}")
            .choice()
                .when(simple("${body} contains 'explode'"))
                .throwException(new IllegalArgumentException("Exchange caused explosion"))
            .endChoice();
    }
}
