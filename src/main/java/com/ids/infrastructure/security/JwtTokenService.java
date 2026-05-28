package com.ids.infrastructure.security;

import com.ids.application.service.usr.TokenProvider;
import com.ids.domain.entity.usr.UsrRole;
import com.ids.domain.entity.usr.UsrUser;
import com.ids.infrastructure.config.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtTokenService implements TokenProvider {
    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    @Override
    public TokenResult createToken(UsrUser user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(jwtProperties.expirationMinutes(), ChronoUnit.MINUTES);
        List<String> roles = user.getRoles().stream().map(UsrRole::getName).sorted().toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.issuer())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(user.getUsername())
                .claim("uid", user.getId())
                .claim("roles", roles)
                .build();

        String value = jwtEncoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(),
                claims
        )).getTokenValue();

        return new TokenResult(value, expiresAt, roles);
    }
}
