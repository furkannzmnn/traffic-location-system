package com.example.trafficlocationsystem.aggregate.resolverType;

import com.example.trafficlocationsystem.aggregate.AbstractAnnotationResolver;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.annotation.Annotation;
import java.util.Map;

public class PostMappingResolver extends AbstractAnnotationResolver {
    @Override
    public Map<String, String> resolve(Annotation annotation, Map<String, String> map) {
        PostMapping postMapping = (PostMapping) annotation;
        map.put("POST", postMapping.value()[0]);
        return map;
    }

    @Override
    public boolean supports(Annotation annotation) {
        return annotation instanceof PostMapping;
    }
}
