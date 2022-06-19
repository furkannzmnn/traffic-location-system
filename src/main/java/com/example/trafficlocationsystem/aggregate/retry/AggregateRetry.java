package com.example.trafficlocationsystem.aggregate.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AggregateRetry {

    private static final Logger LOGGER = LoggerFactory.getLogger(AggregateRetry.class);

    public void retry(Runnable runnable) {
        try {
            start(runnable);
        }catch (Exception e){
            LOGGER.error("NOT RETRY PROCESS FAILED");
        }
    }

    private void start(Runnable runnable) {
        try {
            LOGGER.info("Retry step 1");
            execute(runnable);
        } catch (Exception e) {
            LOGGER.error("retry step 3");
            execute(runnable);
        }
    }

    private void execute(Runnable runnable) {
            LOGGER.info("Retry step 2");
            runnable.run();
    }
}
