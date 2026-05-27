package com.ids.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ids")
public record IdsProperties(
        String corsAllowedOrigins,
        Long alertRefreshPollDelayMs
) {}
