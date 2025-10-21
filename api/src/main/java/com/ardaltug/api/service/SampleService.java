package com.ardaltug.api.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ardaltug.api.config.ProducerFactory;
import com.ardaltug.common.avro.UserAvro;

@Service
public class SampleService {

    private final KafkaTemplate<String, UserAvro> kafkaTemplate;

    // Constructor — create once from your factory
    public SampleService(ProducerFactory producerFactory) {
        // Create it once and reuse it
        this.kafkaTemplate = producerFactory.createProducer("user");
    }

    public void send(UserAvro user) {
        kafkaTemplate.send("user-events", user);
    }
}
