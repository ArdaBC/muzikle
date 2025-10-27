package com.ardaltug.consumer.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ardaltug.common.avro.UserAvro;


@Service
public class SampleService {


    // Use @KafkaListener to subscribe to the topic
    @KafkaListener(
            topics = "user-events",
            groupId = "user-group",
            containerFactory = "userkafkaListenerContainerFactory"
    )
    public void consume(UserAvro user) {
        System.out.println("Received UserAvro message:");
        System.out.println("ID: " + user.getId());
        System.out.println("Name: " + user.getName());
        System.out.println("Password: " + user.getPassword());
    }
}
