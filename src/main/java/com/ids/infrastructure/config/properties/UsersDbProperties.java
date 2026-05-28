package com.ids.infrastructure.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ids.datasource.users")
public record UsersDbProperties(
        String url,
        String username,
        String password,
        String driverName
) {
}