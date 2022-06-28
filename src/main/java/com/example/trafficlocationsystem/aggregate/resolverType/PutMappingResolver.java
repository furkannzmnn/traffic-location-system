package com.example.trafficlocationsystem.aggregate.resolverType;

import com.example.trafficlocationsystem.aggregate.AbstractAnnotationResolver;
import org.springframework.web.bind.annotation.PutMapping;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class PutMappingResolver extends AbstractAnnotationResolver {
    @Override
    public Map<String , String> resolve(Annotation annotation, HashMap<String, String> map) {
        PutMapping putMapping = (PutMapping) annotation;
        map.put("PUT", putMapping.value()[0]);
        return map;
    }

    @Override
    public boolean supports(Annotation annotation) {
        return annotation instanceof PutMapping;
    }
}
