package com.example.trafficlocationsystem.aggregate.preferences;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class BothResolver<T> extends AbstractAggregatePreferences<T> {
    @Override
    public String onlyPreferences(T request, Map<String,String> totalData) {
        if (request instanceof ServletRequest) {
            final HttpServletRequest httpRequest = (HttpServletRequest) request;
            final String header = httpRequest.getHeader("User-Agent");
            totalData.put("deviceName", header);
            totalData.put("location", header); // TODO implement location
            return header;
        }
        return null;
    }

    @Override
    public void logger(String info) {
        LOGGER.info("user info :" + info);
    }
}
