package com.payflow.pg.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
    private String merchantWebhookUrl;
    private String webhookSecret;
    private Card card = new Card();

    @Data
    public static class Card {
        private String baseUrl;
    }
}
