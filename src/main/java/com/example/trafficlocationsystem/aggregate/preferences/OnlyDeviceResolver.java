package com.example.trafficlocationsystem.aggregate.preferences;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class OnlyDeviceResolver<T> extends AbstractAggregatePreferences<T>{
    @Override
    public String onlyPreferences(T request) {
        if (request instanceof ServletRequest) {
            final HttpServletRequest httpRequest = (HttpServletRequest) request;
            return httpRequest.getHeader("User-Agent");
        }
        return EMPTY_DEVICE_NOT_FOUND;
    }

    @Override
    public void logger(String info) {
        LOGGER.info("user info :" + info);
    }
}
