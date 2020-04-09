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

import com.nemisis.standalone.error_handling.shared.FlakyException;
import com.nemisis.standalone.error_handling.shared.FlakyProcessor;
import org.apache.camel.builder.RouteBuilder;


public class DoTryRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:start")
            .to("mock:before")
            .doTry()
                .bean(FlakyProcessor.class)
                .transform(constant("Made it!"))
            .doCatch(FlakyException.class)
                .to("mock:error")
                .transform(constant("Something Bad Happened!"))
            .doFinally()
                .to("mock:finally")
            .end()
            .to("mock:after");

        // TODO: update handled(false) to explicit rethrow
        from("direct:unhandled")
            .to("mock:before")
            .doTry()
                .bean(FlakyProcessor.class)
                .transform(constant("Made it!"))
            .doCatch(FlakyException.class)
                .handled(false)
                .to("mock:error")
                .transform(constant("Something Bad Happened!"))
            .doFinally()
                .to("mock:finally")
            .end()
            .to("mock:after");

        from("direct:conditional")
            .to("mock:before")
            .doTry()
                .bean(FlakyProcessor.class)
                .transform(constant("Made it!"))
            .doCatch(FlakyException.class)
                .onWhen(header("jedi").isNull())
                .to("mock:error")
                .transform(constant("Something Bad Happened!"))
            .doFinally()
                .to("mock:finally")
            .end()
            .to("mock:after");
    }
}
