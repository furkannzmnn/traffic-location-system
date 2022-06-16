package com.example.trafficlocationsystem.aggregate;

import com.example.trafficlocationsystem.aggregate.preferences.AbstractAggregatePreferences;
import com.example.trafficlocationsystem.annatotion.AggregateData;
import com.example.trafficlocationsystem.service.SendDataTopic;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Aspect
@Component
public class AggregateDataAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AggregateDataAspect.class);

    private final SendDataTopic sendDataTopic;

    public AggregateDataAspect(SendDataTopic sendDataTopic) {
        this.sendDataTopic = sendDataTopic;
    }


    @Around("@annotation(com.example.trafficlocationsystem.annatotion.AggregateData)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        CompletableFuture.runAsync(() -> sendData(joinPoint));
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

