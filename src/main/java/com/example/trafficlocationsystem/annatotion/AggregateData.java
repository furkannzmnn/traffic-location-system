package com.example.trafficlocationsystem.annatotion;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AggregateData {

    boolean onlyDevice() default false;
    boolean onlyLocation() default false;


}
