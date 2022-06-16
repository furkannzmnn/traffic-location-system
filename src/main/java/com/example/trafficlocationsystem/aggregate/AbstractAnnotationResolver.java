package com.example.trafficlocationsystem.aggregate;

import com.example.trafficlocationsystem.aggregate.resolverType.*;
import com.example.trafficlocationsystem.annatotion.AggregateData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.util.Map;

public abstract class AbstractAnnotationResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAnnotationResolver.class);

    public static AbstractAnnotationResolver getResolver(Annotation annotationType) {
        if (annotationType instanceof DeleteMapping) {
            return new DeleteMappingResolver();
        } else if (annotationType instanceof PatchMapping) {
            return new PatchMappingResolver();
        }else if (annotationType instanceof GetMapping) {
            return new GetMappingResolver();
        }else if (annotationType instanceof PostMapping) {
            return new PostMappingResolver();
        }else if (annotationType instanceof PutMapping) {
            return new PutMappingResolver();
        }else if (annotationType instanceof AggregateData) {
            return null;
        }else {
            LOGGER.error("Unsupported annotation type: {} " , annotationType.getClass().getName());
            throw new UnsupportedOperationException("Unsupported annotation type: " + annotationType.getClass().getName());
        }
    }

    public Map<Integer, String> type(Annotation annotation) {
        if (supports(annotation)) {
            return resolve(annotation);
        }
        return null;
    }

    public abstract Map<Integer, String> resolve(Annotation annotation);
    public abstract boolean supports(Annotation annotation);

}
