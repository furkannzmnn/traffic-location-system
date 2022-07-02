package com.example.trafficlocationsystem.aggregate;

import com.example.trafficlocationsystem.aggregate.preferences.AbstractAggregatePreferences;
import com.example.trafficlocationsystem.aggregate.retry.AggregateRetry;
import com.example.trafficlocationsystem.annatotion.AggregateData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class AggregateDataAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AggregateDataAspect.class);

    private final AggregateRetry aggregateRetry;
    private final ThreadPoolTaskScheduler taskScheduler;
    private final ObjectMapper objectMapper;
    private final Map<String, String> totalData = new ConcurrentHashMap<>();


    public AggregateDataAspect(AggregateRetry aggregateRetry, ThreadPoolTaskScheduler taskScheduler, ObjectMapper objectMapper) {
        this.aggregateRetry = aggregateRetry;
        this.taskScheduler = taskScheduler;
        this.objectMapper = objectMapper;
    }


    @Around("@annotation(com.example.trafficlocationsystem.annatotion.AggregateData)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable  {
        try {
            CompletableFuture.runAsync(() -> sendData(joinPoint));
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
        final boolean onlyLocation = aggregateData.onlyLocation();

            Object[] signatureArgs = joinPoint.getArgs();
            for (Object each : signatureArgs) {
                final AbstractAggregatePreferences<Object> preferences = AbstractAggregatePreferences.resolvePreferencesType(onlyDevice, onlyLocation) ;
                preferences.completeProcess(each, totalData);
            }


        Annotation[] annotations = methodSignature.getMethod().getAnnotations();

        try {
            for (Annotation annotation : annotations) {
                AbstractAnnotationResolver abstractAnnotationResolver = AbstractAnnotationResolver.getResolver(annotation);
                if (abstractAnnotationResolver != null) {
                    Map<String , String> map = abstractAnnotationResolver.type(annotation, totalData);
                    if (map != null) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            LOGGER.info("Key: " + entry.getKey() + " Value: " + entry.getValue());
                            // to json
                            String json = objectMapper.writeValueAsString(entry.getValue());
                            LOGGER.info("Json: " + json);
                        }
                    }
                }
            }
        }catch (UnsupportedOperationException e) {
            LOGGER.error("Error: " + e.getMessage());
        } catch (JsonProcessingException e) {
            LOGGER.error("OPS!");
        }
    }
}

