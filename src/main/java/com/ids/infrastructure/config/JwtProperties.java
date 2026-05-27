package com.ids.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ids.jwt")
public record JwtProperties(String issuer, String secret, long expirationMinutes) {}
