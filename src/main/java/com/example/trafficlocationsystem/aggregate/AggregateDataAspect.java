package com.example.trafficlocationsystem.aggregate;

import com.example.trafficlocationsystem.aggregate.preferences.AbstractAggregatePreferences;
import com.example.trafficlocationsystem.aggregate.retry.AggregateRetry;
import com.example.trafficlocationsystem.annatotion.AggregateData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

@Aspect
@Component
public class AggregateDataAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AggregateDataAspect.class);

    private final AggregateRetry aggregateRetry;
    private final ThreadPoolTaskScheduler taskScheduler;

    public AggregateDataAspect(AggregateRetry aggregateRetry, ThreadPoolTaskScheduler taskScheduler) {
        this.aggregateRetry = aggregateRetry;
        this.taskScheduler = taskScheduler;
    }


    @Around("@annotation(com.example.trafficlocationsystem.annatotion.AggregateData)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            sendData(joinPoint);
        }catch (Exception e){
            taskScheduler.submit(() -> aggregateRetry.retry(() -> sendData(joinPoint)));
        }
        return joinPoint.proceed();
    }


    private void sendData(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        // get method annotations
        AggregateData aggregateData = methodSignature.getMethod().getAnnotation(AggregateData.class);

        final boolean onlyDevice = aggregateData.onlyDevice();

        if (onlyDevice) {
            LOGGER.info("Only device");
            Object[] signatureArgs = joinPoint.getArgs();
            for (Object each : signatureArgs) {
                final AbstractAggregatePreferences<Object> preferences = AbstractAggregatePreferences.resolvePreferencesType(onlyDevice);
                preferences.completeProcess(each);
            }
        }

        Annotation[] annotations = methodSignature.getMethod().getAnnotations();


        try {
            for (Annotation annotation : annotations) {
                AbstractAnnotationResolver abstractAnnotationResolver = AbstractAnnotationResolver.getResolver(annotation);
                if (abstractAnnotationResolver != null) {
                    Map<Integer, String> map = abstractAnnotationResolver.type(annotation);
                    if (map != null) {
                       // sendDataTopic.sendDataFromKafka(map);
                        for (Map.Entry<Integer, String> entry : map.entrySet()) {
                            LOGGER.info("Key: " + entry.getKey() + " Value: " + entry.getValue());
                        }
                    }
                }
            }
        }catch (UnsupportedOperationException e) {
            LOGGER.error("Error: " + e.getMessage());
        }
    }
}

