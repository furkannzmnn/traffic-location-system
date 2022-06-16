package com.example.trafficlocationsystem.service;

import org.apache.kafka.common.protocol.types.Field;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Map;

@Service
public class SendDataTopic {

    private final KafkaTemplate<String, Map<Integer, String>> kafkaTemplate;

    public SendDataTopic(KafkaTemplate<String, Map<Integer, String>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendData(String data) {
        System.out.println("Send data to topic: " + data);
    }

    public void sendDataFromKafka(Object data) {
        Map<Integer, String> map = (Map<Integer, String>) data;

        final ListenableFuture<SendResult<String, Map<Integer, String>>> listenableFuture = kafkaTemplate.send("traffic-location-system", map);
        listenableFuture.addCallback(result -> System.out.println("Send data to topic: " + map), throwable -> System.out.println("Error: " + throwable.getMessage()));
    }
}
