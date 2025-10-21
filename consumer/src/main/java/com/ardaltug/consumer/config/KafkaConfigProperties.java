package com.ardaltug.consumer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfigProperties {

    private Map<String, ConsumerSettings> consumers;

    @Data
    public static class ConsumerSettings {
        private String bootstrapServers;
        private String topic;
        private String groupId;
        private int concurrency;
        private int maxPollRecords;
        private String autoOffsetReset;
    }
}
