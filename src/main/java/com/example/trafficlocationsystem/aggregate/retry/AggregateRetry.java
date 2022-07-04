package com.example.trafficlocationsystem.aggregate.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AggregateRetry {

    private static final Logger LOGGER = LoggerFactory.getLogger(AggregateRetry.class);

    private transient byte retryCount = 0;

    public void retry(Runnable runnable) {
        try {
           TaskRetry taskRetry = (task) -> runnable.run();
           taskRetry.retry(runnable);
        }catch (Exception e){
            this.retryCount++;
            if(this.retryCount <= 3){
                LOGGER.error("Retry count: {}", this.retryCount);
                this.retry(runnable);
            }else{
                LOGGER.error("NOT RETRY PROCESS FAILED");
            }
        }
    }
}
