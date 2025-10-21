package com.ardaltug.api.controller;

import com.ardaltug.api.config.ProducerFactory;
import com.ardaltug.common.avro.UserAvro;

import java.time.Instant;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    private final ProducerFactory producerFactory;

    public SampleController(ProducerFactory producerFactory) {
        this.producerFactory = producerFactory;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from SampleController!";
    }

    @GetMapping("/send-sample")
    public String sendSampleMessage() {
        // Create a Kafka producer for the "user" producer defined in application.yaml
        KafkaTemplate<String, UserAvro> kafkaTemplate = producerFactory.createProducer("user");

         UserAvro user = UserAvro.newBuilder()
                .setId("1")
                .setName("Arda")
                .setPassword("secret")  // Ideally passwords should be hashed
                .build();

        // Send to topic defined in KafkaConfigProperties
        kafkaTemplate.send("user-events", user);

        return "Message sent to Kafka!";
    }
}
