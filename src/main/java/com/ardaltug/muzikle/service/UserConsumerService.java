package com.ardaltug.muzikle.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ardaltug.muzikle.avro.UserAvro;
import com.ardaltug.muzikle.model.User;
import com.ardaltug.muzikle.repository.UserRepository; // Your Avro generated class

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserConsumerService {

    private static final Logger log = LoggerFactory.getLogger(UserConsumerService.class);

    private final UserRepository userRepository;

    @KafkaListener(
        topics = "${kafka.consumers.user.topic}",
        groupId = "${kafka.consumers.user.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(UserAvro userAvro) {
        try {
            log.info("Consumed user event for id={} name={}", userAvro.getId(), userAvro.getName());

            

            // Map Avro to JPA entity
            User user = new User();
            user.setId(userAvro.getId().toString());
                    user.setName(userAvro.getName().toString());
                    user.setPassword(userAvro.getPassword().toString());
                    // Use helper function for date conversion
                    user.setSignUpDate(convertInstantToLocalDateTime(userAvro.getSignUpDate()));
                    user.setLoginDate(convertInstantToLocalDateTime(userAvro.getLoginDate()));


            // Persist the entity
            userRepository.save(user);
            log.info("Persisted user id={}", user.getId());

        } catch (Exception e) {
            log.error("Failed to persist user: {}", userAvro, e);
        }
    }

    // Helper to convert Instant to LocalDateTime safely
    private LocalDateTime convertInstantToLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
