package com.ids.application.dto;

import java.time.Instant;
import java.util.List;

public record AuthResult(
        String accessToken,
        String tokenType,
        Instant expiresAt,
        String username,
        List<String> roles
) {}
