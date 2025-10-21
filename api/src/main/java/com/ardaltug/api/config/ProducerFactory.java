package com.ardaltug.api.config;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProducerFactory {

    private final KafkaConfigProperties kafkaProperties;

    public ProducerFactory(KafkaConfigProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    public <T> KafkaTemplate<String, T> createProducer(String producerName) {
        KafkaConfigProperties.ProducerSettings settings = kafkaProperties.getProducers().get(producerName);

        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, settings.getBootstrapServers());
        config.put(ProducerConfig.ACKS_CONFIG, settings.getAcks());
        config.put(ProducerConfig.RETRIES_CONFIG, settings.getRetries());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);

        config.put("schema.registry.url", "http://localhost:8081");

        config.put("specific.avro.reader", true);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(config));
    }
}
