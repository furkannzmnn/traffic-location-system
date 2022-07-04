package com.example.trafficlocationsystem.aggregate.resolverType;

import com.example.trafficlocationsystem.aggregate.AbstractAnnotationResolver;
import org.springframework.web.bind.annotation.PatchMapping;

import java.lang.annotation.Annotation;
import java.util.Map;

public class PatchMappingResolver extends AbstractAnnotationResolver {
    @Override
    public Map<String, String> resolve(Annotation annotation, Map<String, String> map) {
        PatchMapping patchMapping = (PatchMapping) annotation;
        map.put("PATCH", patchMapping.value()[0]);
        return map;
    }

    @Override
    public boolean supports(Annotation annotation) {
        throw new UnsupportedOperationException();
    }
}
