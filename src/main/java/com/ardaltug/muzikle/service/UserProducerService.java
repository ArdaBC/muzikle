package com.ardaltug.muzikle.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ardaltug.muzikle.avro.UserAvro;
import com.ardaltug.muzikle.config.KafkaProperties;
import com.ardaltug.muzikle.model.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserProducerService  {

    private static final Logger log = LoggerFactory.getLogger(UserProducerService.class);

    @Qualifier("userProducer")
    private final KafkaProducer<String, UserAvro> producer;

    private final KafkaProperties kafkaProperties;

    /**
     * Send a message asynchronously
     */
    public void sendMessage(User value) {
        String topic = kafkaProperties.getProducers().get("user").getTopic();

        // Map POJO User → Avro UserAvro
        UserAvro avroValue = mapToAvro(value);

        
        ProducerRecord<String, UserAvro> record = new ProducerRecord<>(topic, value.getId(), avroValue);


        producer.send(record, (RecordMetadata metadata, Exception exception) -> {
            if (exception != null) {
                log.error("Failed to send message with key={} value={}", value.getId(), avroValue, exception);
            } else {
                log.info("Sent message to topic={} partition={} offset={}",
                        metadata.topic(), metadata.partition(), metadata.offset());
            }
        });
    }

    private Instant toInstantOrNull(LocalDateTime dateTime) {
    if (dateTime == null) {
        return null;
    }
    return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

     private UserAvro mapToAvro(User u) {
        // generated Avro class has builder pattern: UserAvro.newBuilder()
        return UserAvro.newBuilder()
                .setId(u.getId())
                .setName(u.getName())
                .setPassword(u.getPassword()) // warning: don't send plain passwords in prod
                .setSignUpDate(toInstantOrNull(u.getSignUpDate()))
                .setLoginDate(toInstantOrNull(u.getLoginDate()))
                .build();
    }

    /**
     * Send a message synchronously with timeout
     */
    /*   Commented for Testing
    public void sendMessageSync(String key, String value, long timeoutMs) {
        try {
            String topic = kafkaProperties.getProducers().get("user").getTopic();
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
            producer.send(record).get(timeoutMs, TimeUnit.MILLISECONDS);
            log.info("Sync message sent: key={} value={}", key, value);
        } catch (Exception e) {
            log.error("Failed to send sync message with key={} value={}", key, value, e);
        }
    }*/
}

