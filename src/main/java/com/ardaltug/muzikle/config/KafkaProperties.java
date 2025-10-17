package com.ardaltug.muzikle.config;

import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {

    private Map<String, ProducerSettings> producers;
    private Map<String, ConsumerSettings> consumers;

    //Precreated Settings data for ProducerConfig
    @Data
    public static class ProducerSettings {
        @NotBlank private String bootstrapServers;
        @NotBlank private String topic;
        private String acks = "all";
        private boolean idempotence = true;

        @Min(0) private int retries = 5;
        @Min(1) private int maxInFlight = 1;
        @Min(0) private int batchSize = 16 * 1024;
        @Min(0) private int lingerMs = 5;
        private String compression = "lz4";
    }


    //Precreated Settings data for ConsumerConfig
    @Data
    public static class ConsumerSettings {
        @NotBlank private String bootstrapServers;
        @NotBlank private String topic;
        @NotBlank private String groupId = "default-group";
        private boolean enableAutoCommit = false; // recommended false for controlled commits
        private String autoOffsetReset = "earliest"; // earliest | latest
        @Min(1) private int concurrency = 1;
        @Min(0) private int maxPollRecords = 500;
    }
}
