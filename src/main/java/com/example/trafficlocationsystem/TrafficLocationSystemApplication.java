package com.example.trafficlocationsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class TrafficLocationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrafficLocationSystemApplication.class, args);
    }

}
