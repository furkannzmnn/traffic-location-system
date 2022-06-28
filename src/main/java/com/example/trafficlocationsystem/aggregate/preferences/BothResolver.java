package com.example.trafficlocationsystem.aggregate.preferences;

import java.util.Map;

public class BothResolver<T> extends AbstractAggregatePreferences<T> {
    @Override
    public String onlyPreferences(T request, Map<String,String> totalData) {
        return null;
    }

    @Override
    public void logger(String info) {

    }
}
