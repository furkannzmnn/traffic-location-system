package com.example.trafficlocationsystem.aggregate.resolverType;

import com.example.trafficlocationsystem.aggregate.AbstractAnnotationResolver;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class DeleteMappingResolver extends AbstractAnnotationResolver {
    @Override
    public Map<Integer, String> resolve(Annotation annotation) {
        DeleteMapping deleteMapping = (DeleteMapping) annotation;
        Map<Integer, String> map = new ConcurrentHashMap<>();
        map.put(new Random().nextInt(), deleteMapping.value()[0]);
        return map;
    }

    @Override
    public boolean supports(Annotation annotation) {
        return annotation instanceof DeleteMapping;
    }
}
