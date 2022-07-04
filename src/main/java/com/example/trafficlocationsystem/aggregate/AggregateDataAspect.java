package com.example.trafficlocationsystem.aggregate;

import com.example.trafficlocationsystem.aggregate.preferences.AbstractAggregatePreferences;
import com.example.trafficlocationsystem.aggregate.retry.AggregateRetry;
import com.example.trafficlocationsystem.annatotion.AggregateData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Stream;

@Aspect
@Component
public class AggregateDataAspect {

    private static final Logger LOGGER = LogManager.getLogger(AggregateDataAspect.class);

    private final AggregateRetry aggregateRetry;
    private final ThreadPoolTaskScheduler taskScheduler;
    private final Map<String, String> totalData = new ConcurrentHashMap<>();


    public AggregateDataAspect(AggregateRetry aggregateRetry, ThreadPoolTaskScheduler taskScheduler) {
        this.aggregateRetry = aggregateRetry;
        this.taskScheduler = taskScheduler;
    }

    @Around("@annotation(com.example.trafficlocationsystem.annatotion.AggregateData)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            CompletableFuture.runAsync(() -> sendData(joinPoint));
        } catch (Exception e) {
            if (e instanceof UnsupportedOperationException) {
                e.printStackTrace();
                return joinPoint.proceed();
            }
            taskScheduler.submit(() -> aggregateRetry.retry(() -> sendData(joinPoint)));
        }
        return joinPoint.proceed();
    }


    private void sendData(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        // get method annotations
        AggregateData aggregateData = methodSignature.getMethod().getAnnotation(AggregateData.class);

        final boolean onlyDevice = aggregateData.onlyDevice();
        final boolean onlyLocation = aggregateData.onlyLocation();

        Object[] signatureArgs = joinPoint.getArgs();
        for (Object each : signatureArgs) {
            final AbstractAggregatePreferences<Object> preferences = AbstractAggregatePreferences.resolvePreferencesType(onlyDevice, onlyLocation);
            preferences.completeProcess(each, totalData);
        }

        Annotation[] annotations = methodSignature.getMethod().getAnnotations();
        try {
            for (Annotation annotation : annotations) {
                AbstractAnnotationResolver abstractAnnotationResolver = AbstractAnnotationResolver.getResolver(annotation);
                if (abstractAnnotationResolver != null) {
                    Map<String, String> apiData = abstractAnnotationResolver.type(annotation, totalData);

                    Stream.of(totalData, apiData)
                            .flatMap(key -> key.entrySet().stream()
                            .map(entry -> entry.getKey() + ":" + entry.getValue()))
                            .forEach(LOGGER::info);
                }
            }
        } catch (UnsupportedOperationException e) {
            LOGGER.error("Error: " + e.getMessage());
        }
    }
}

