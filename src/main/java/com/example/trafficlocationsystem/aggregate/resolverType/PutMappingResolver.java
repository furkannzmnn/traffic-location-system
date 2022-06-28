package com.example.trafficlocationsystem.aggregate.resolverType;

import com.example.trafficlocationsystem.aggregate.AbstractAnnotationResolver;
import org.springframework.web.bind.annotation.PutMapping;

import java.lang.annotation.Annotation;
import java.util.Map;

public class PutMappingResolver extends AbstractAnnotationResolver {
    @Override
    public Map<String , String> resolve(Annotation annotation, Map<String, String> map) {
        PutMapping putMapping = (PutMapping) annotation;
        map.put("PUT", putMapping.value()[0]);
        return map;
    }

    @Override
    public boolean supports(Annotation annotation) {
        return annotation instanceof PutMapping;
    }
}
