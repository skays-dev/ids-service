package com.ids.infrastructure.security;

import com.ids.application.service.usr.TokenProvider;
import com.ids.infrastructure.config.properties.JwtProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import static com.ids.testsupport.IdsTestFixtures.role;
import static com.ids.testsupport.IdsTestFixtures.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

    @Mock
    private JwtEncoder jwtEncoder;

    private final JwtProperties jwtProperties = new JwtProperties("ids-service", "test-secret", 60L);

    @Test
    void createToken_shouldEncodeJwtAndReturnSortedRoles() {
        JwtTokenService service = new JwtTokenService(jwtEncoder, jwtProperties);
        when(jwtEncoder.encode(any())).thenReturn(Jwt.withTokenValue("jwt-token")
                .header("alg", "HS256")
                .claim("sub", "admin")
                .build());

        TokenProvider.TokenResult result = service.createToken(
                user(1L, "admin", true, role(1L, "ROLE_USER"), role(2L, "ROLE_ADMIN"))
        );

        assertThat(result.token()).isEqualTo("jwt-token");
        assertThat(result.expiresAt()).isAfter(java.time.Instant.now());
        assertThat(result.roles()).containsExactly("ROLE_ADMIN", "ROLE_USER");
    }
}
