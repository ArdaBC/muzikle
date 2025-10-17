package com.ardaltug.muzikle.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import com.ardaltug.muzikle.avro.UserAvro;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, UserAvro> userConsumerFactory() {
        KafkaProperties.ConsumerSettings settings = kafkaProperties.getConsumers().get("user");


        Map<String, Object> props = new HashMap<>();
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, settings.getBootstrapServers());
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, settings.getGroupId());
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //Kafka Avro Part
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put("schema.registry.url", "http://localhost:8081");
        props.put("specific.avro.reader", true); // if using generated class
        // Consumer best-practices (can adjust per workload)
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, settings.isEnableAutoCommit());
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, settings.getAutoOffsetReset());
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG, settings.getMaxPollRecords());

        return new DefaultKafkaConsumerFactory<>(props);
    }

   @Bean
public ConcurrentKafkaListenerContainerFactory<String, UserAvro> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, UserAvro> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(userConsumerFactory());
    factory.setConcurrency(kafkaProperties.getConsumers().get("user").getConcurrency());
    return factory;
}
}
