package com.ardaltug.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ardaltug.api.service.SampleService;
import com.ardaltug.common.avro.UserAvro;

@RestController
public class SampleController {

    private final SampleService sampleService; // ✅ add this

    public SampleController(SampleService sampleService) { // ✅ constructor injection
        this.sampleService = sampleService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from SampleController!";
    }

    @GetMapping("/send-sample")
    public String sendSampleMessage() {

            UserAvro aaaa = UserAvro.newBuilder()
            .setId("3131")
            .setName("Arda")
            .setPassword("secret")
            .build();
            sampleService.send(aaaa);
        return "Message sent at " + java.time.LocalDateTime.now();
    }
}