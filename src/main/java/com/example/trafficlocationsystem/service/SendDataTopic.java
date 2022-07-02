package com.example.trafficlocationsystem.service;

import org.springframework.stereotype.Service;

@Service
public class SendDataTopic {

    public void sendData(String data) {
        System.out.println("Send data to topic: " + data);
    }
}
