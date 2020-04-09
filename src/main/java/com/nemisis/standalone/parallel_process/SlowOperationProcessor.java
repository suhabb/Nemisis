package com.nemisis.standalone.parallel_process;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SlowOperationProcessor implements AsyncProcessor {

    private final Logger log = LoggerFactory.getLogger(SlowOperationProcessor.class);
    private final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();

    @Override
    public boolean process(final Exchange exchange, final AsyncCallback asyncCallback) {

        final boolean completedSynchronously = false;

        backgroundExecutor.submit(new Runnable() {
            @Override
            public void run() {
                log.info("Running operation asynchronously");
                try {
                    log.info("Doing something slowly");
                    Thread.sleep(200); // this runs slowly
                    log.info("...done");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                // the current thread will continue to process the exchange
                // through the remainder of the route
                asyncCallback.done(completedSynchronously);
            }
        });

        return completedSynchronously;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        throw new IllegalStateException("Should never be called");
    }
}