package com.ardaltug.consumer.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import com.ardaltug.consumer.config.ConsumerFactory;
import com.ardaltug.common.avro.UserAvro;


@Service
public class SampleService {

    private final ConcurrentKafkaListenerContainerFactory<String, UserAvro> kafkaListenerFactory;

    public SampleService(ConsumerFactory consumerFactory) {
        // Create listener factory for the "user" consumer defined in application.yaml
        this.kafkaListenerFactory = consumerFactory.createConsumer("user");
    }

    // Use @KafkaListener to subscribe to the topic
    @KafkaListener(
            topics = "user-events",
            groupId = "user-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(UserAvro user) {
        System.out.println("Received UserAvro message:");
        System.out.println("ID: " + user.getId());
        System.out.println("Name: " + user.getName());
        System.out.println("Password: " + user.getPassword());
    }
}
