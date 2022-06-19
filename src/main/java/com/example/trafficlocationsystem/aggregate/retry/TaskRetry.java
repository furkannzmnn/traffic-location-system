package com.example.trafficlocationsystem.aggregate.retry;

@FunctionalInterface
public interface TaskRetry {
    void retry(Runnable runnable);
}
