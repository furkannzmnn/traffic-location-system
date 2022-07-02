package com.example.trafficlocationsystem.annatotion;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @apiNote THE MUST BE USING HTTP SERVLET REQUEST
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AggregateData {

    boolean onlyDevice() default false;
    boolean onlyLocation() default false;


}
