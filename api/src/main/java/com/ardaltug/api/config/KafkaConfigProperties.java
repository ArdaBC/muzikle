package com.ardaltug.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfigProperties {

    private Map<String, ProducerSettings> producers;

    @Data
    public static class ProducerSettings {
        private String bootstrapServers;
        private String topic;
        private String acks;
        private int retries;
    }
}
