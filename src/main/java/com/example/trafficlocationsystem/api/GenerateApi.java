package com.example.trafficlocationsystem.api;

import com.example.trafficlocationsystem.annatotion.AggregateData;
import com.example.trafficlocationsystem.service.SendDataTopic;

import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class GenerateApi {

    private final SendDataTopic sendDataTopic;

    public GenerateApi(SendDataTopic sendDataTopic) {
        this.sendDataTopic = sendDataTopic;
    }

    @AggregateData(onlyLocation = true)
    @PostMapping("/sendData")
    public ResponseEntity<?> generateData(HttpServletRequest request, HttpServletResponse response) {
        sendDataTopic.sendData("data");
        return ResponseEntity.ok("ok");
    }
}
