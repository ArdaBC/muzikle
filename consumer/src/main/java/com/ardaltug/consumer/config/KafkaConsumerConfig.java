package com.ardaltug.consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import com.ardaltug.common.avro.UserAvro;


@Configuration
public class KafkaConsumerConfig {


private final ConsumerFactory consumerFactory;


    public KafkaConsumerConfig(ConsumerFactory consumerFactory) {
        this.consumerFactory = consumerFactory;
    }


    //used by @KafkaListener Change To Need
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserAvro> userkafkaListenerContainerFactory() {
        return consumerFactory.createConsumer("user");
    }
}