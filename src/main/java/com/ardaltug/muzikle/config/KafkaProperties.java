package com.ardaltug.muzikle.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {

    private Map<String, ProducerSettings> producers;

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
}
