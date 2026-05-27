package com.ids.application.service;

import com.ids.domain.entity.AppUser;

import java.time.Instant;
import java.util.List;

public interface TokenProvider {
    TokenResult createToken(AppUser user);

    record TokenResult(String token, Instant expiresAt, List<String> roles) {}
}
