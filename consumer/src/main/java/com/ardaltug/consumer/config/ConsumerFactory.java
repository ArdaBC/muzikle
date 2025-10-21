package com.ardaltug.consumer.config;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ConsumerFactory {

    private final KafkaConfigProperties kafkaProperties;

    public ConsumerFactory(KafkaConfigProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    public <T> ConcurrentKafkaListenerContainerFactory<String, T> createConsumer(String consumerName) {
        KafkaConfigProperties.ConsumerSettings settings = kafkaProperties.getConsumers().get(consumerName);

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, settings.getBootstrapServers());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, settings.getGroupId());
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, settings.getAutoOffsetReset());
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, settings.getMaxPollRecords());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        config.put("schema.registry.url", "http://localhost:8081");
        config.put("specific.avro.reader", true);

        DefaultKafkaConsumerFactory<String, T> factory = new DefaultKafkaConsumerFactory<>(config);

        ConcurrentKafkaListenerContainerFactory<String, T> listenerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        listenerFactory.setConsumerFactory(factory);
        listenerFactory.setConcurrency(settings.getConcurrency());
        return listenerFactory;
    }
}
