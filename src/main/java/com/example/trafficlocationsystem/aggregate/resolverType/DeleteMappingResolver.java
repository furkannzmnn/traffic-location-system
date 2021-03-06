package com.example.trafficlocationsystem.aggregate.resolverType;

import com.example.trafficlocationsystem.aggregate.AbstractAnnotationResolver;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.lang.annotation.Annotation;
import java.util.Map;

public class DeleteMappingResolver extends AbstractAnnotationResolver {
    @Override
    public Map<String, String> resolve(Annotation annotation, Map<String, String> map ) {
        DeleteMapping deleteMapping = (DeleteMapping) annotation;
        map.put("DELETE", deleteMapping.value()[0]);
        return map;
    }

    @Override
    public boolean supports(Annotation annotation) {
        return annotation instanceof DeleteMapping;
    }
}
