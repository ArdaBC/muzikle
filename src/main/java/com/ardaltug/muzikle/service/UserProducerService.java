package com.ardaltug.muzikle.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ardaltug.muzikle.config.KafkaProperties;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class UserProducerService  {

    //private static final Logger log = LoggerFactory.getLogger(UserProducer.class);

    @Qualifier("userProducer")
    private final KafkaProducer<String, String> producer;

    private final KafkaProperties kafkaProperties;

    /**
     * Send a message asynchronously
     */
    public void sendMessage(String key, String value) {
        String topic = kafkaProperties.getProducers().get("user").getTopic();
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);

        producer.send(record, (RecordMetadata metadata, Exception exception) -> {
            if (exception != null) {
                /*
                log.error("Failed to send message with key={} value={}", key, value, exception);
                */
            } else {
                /*
                log.info("Sent message to topic={} partition={} offset={}",
                        metadata.topic(), metadata.partition(), metadata.offset());
                */
            }
        });
    }

    /**
     * Send a message synchronously with timeout
     */
    public void sendMessageSync(String key, String value, long timeoutMs) {
        try {
            String topic = kafkaProperties.getProducers().get("user").getTopic();
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
            producer.send(record).get(timeoutMs, TimeUnit.MILLISECONDS);
            //log.info("Sync message sent: key={} value={}", key, value);
        } catch (Exception e) {
            //log.error("Failed to send sync message with key={} value={}", key, value, e);
        }
    }
}

