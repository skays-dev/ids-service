package com.ids.application.usecase.usr;

import com.ids.application.dto.usr.AuthResult;
import com.ids.application.service.usr.TokenProvider;
import com.ids.domain.entity.usr.UsrRole;
import com.ids.domain.entity.usr.UsrUser;
import com.ids.domain.repository.usr.UsrUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.ids.testsupport.IdsTestFixtures.role;
import static com.ids.testsupport.IdsTestFixtures.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserUseCaseTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsrUserRepository usrUserRepository;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private AuthenticateUserUseCase useCase;

    @Test
    void execute_shouldAuthenticateLoadUserAndCreateBearerResult() {
        UsrRole role = role(1L, "ROLE_ADMIN");
        UsrUser user = user(10L, "admin", true, role);
        Instant expiresAt = Instant.parse("2026-05-28T12:00:00Z");
        when(usrUserRepository.findByUsernameIgnoreCase("admin")).thenReturn(Optional.of(user));
        when(tokenProvider.createToken(user)).thenReturn(new TokenProvider.TokenResult("token-value", expiresAt, List.of("ROLE_ADMIN")));

        AuthResult result = useCase.execute("admin", "secret");

        assertThat(result.accessToken()).isEqualTo("token-value");
        assertThat(result.tokenType()).isEqualTo("Bearer");
        assertThat(result.expiresAt()).isEqualTo(expiresAt);
        assertThat(result.username()).isEqualTo("admin");
        assertThat(result.roles()).containsExactly("ROLE_ADMIN");

        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(captor.capture());
        assertThat(captor.getValue().getPrincipal()).isEqualTo("admin");
        assertThat(captor.getValue().getCredentials()).isEqualTo("secret");
        verify(tokenProvider).createToken(user);
    }

    @Test
    void execute_shouldFailWhenUserCannotBeReloadedAfterAuthentication() {
        when(usrUserRepository.findByUsernameIgnoreCase("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute("ghost", "secret"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found after authentication");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
