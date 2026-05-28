package com.ids.application.service.usr;

import com.ids.domain.entity.usr.UsrUser;

import java.time.Instant;
import java.util.List;

public interface TokenProvider {
    TokenResult createToken(UsrUser user);

    record TokenResult(String token, Instant expiresAt, List<String> roles) {}
}
