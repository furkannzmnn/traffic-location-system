package com.example.trafficlocationsystem.aggregate.preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class AbstractAggregatePreferences<T> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractAggregatePreferences.class);
    protected static final String EMPTY_DEVICE_NOT_FOUND = "NOT RESOLVER DEVICE INFO";

    public void completeProcess(T request, Map<String, String> totalData) {
        final String info = onlyPreferences(request,totalData);
        logger(info);
    }

    public static <T> AbstractAggregatePreferences<T> resolvePreferencesType(boolean isDevice, boolean isLocation) {
        if (isDevice && isLocation) return new BothResolver<>();
        if (isDevice) return new OnlyDeviceResolver<>();
        else if (isLocation) return new OnlyLocationResolver<>();
        throw new UnsupportedOperationException();
    }

    public abstract String onlyPreferences(T request, Map<String, String> totalData);
    public abstract void logger(String info);
}
