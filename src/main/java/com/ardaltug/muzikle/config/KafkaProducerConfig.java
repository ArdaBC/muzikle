package com.ardaltug.muzikle.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean(name = "userProducer", destroyMethod = "close")
    public KafkaProducer<String, String> userProducer() {
        return createProducer(kafkaProperties.getProducers().get("user"));
    }

    /*
    @Bean(name = "playlistProducer", destroyMethod = "close")
    public KafkaProducer<String, String> playlistProducer() {
        return createProducer(kafkaProperties.getProducers().get("playlist"));
    }
    */

    private KafkaProducer<String, String> createProducer(KafkaProperties.ProducerSettings settings) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, settings.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, settings.getAcks());
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, Boolean.toString(settings.isIdempotence()));
        props.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(settings.getRetries()));
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, Integer.toString(settings.getMaxInFlight()));
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(settings.getBatchSize()));
        props.put(ProducerConfig.LINGER_MS_CONFIG, Integer.toString(settings.getLingerMs()));
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, settings.getCompression());

        return new KafkaProducer<>(props);
    }
}
