package com.ids.infrastructure.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ids.datasource.main")
public record IdsDbProperties(
        String url,
        String username,
        String password,
        String driverName
) {
}