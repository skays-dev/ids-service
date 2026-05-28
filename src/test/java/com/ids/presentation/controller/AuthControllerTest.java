package com.ids.presentation.controller;

import com.ids.application.dto.usr.AuthResult;
import com.ids.application.dto.usr.CurrentUserDto;
import com.ids.application.usecase.usr.AuthenticateUserUseCase;
import com.ids.application.usecase.usr.GetCurrentUserUseCase;
import com.ids.presentation.request.LoginRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticateUserUseCase authenticateUserUseCase;

    @Mock
    private GetCurrentUserUseCase getCurrentUserUseCase;

    @InjectMocks
    private AuthController controller;

    @Test
    void login_shouldDelegateToAuthenticateUseCase() {
        AuthResult authResult = new AuthResult("token", "Bearer", Instant.parse("2026-05-28T12:00:00Z"), "admin", List.of("ROLE_ADMIN"));
        when(authenticateUserUseCase.execute("admin", "secret")).thenReturn(authResult);

        AuthResult result = controller.login(new LoginRequest("admin", "secret"));

        assertThat(result).isSameAs(authResult);
        verify(authenticateUserUseCase).execute("admin", "secret");
    }

    @Test
    void oauthToken_shouldSupportPasswordGrantOnly() {
        AuthResult authResult = new AuthResult("token", "Bearer", Instant.parse("2026-05-28T12:00:00Z"), "admin", List.of("ROLE_ADMIN"));
        when(authenticateUserUseCase.execute("admin", "secret")).thenReturn(authResult);

        assertThat(controller.oauthToken("admin", "secret", "password")).isSameAs(authResult);

        assertThatThrownBy(() -> controller.oauthToken("admin", "secret", "client_credentials"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Only grant_type=password");
    }

    @Test
    void me_shouldDelegateToCurrentUserUseCase() {
        var authentication = new UsernamePasswordAuthenticationToken("admin", "n/a");
        CurrentUserDto dto = new CurrentUserDto("admin", List.of("ROLE_ADMIN"));
        when(getCurrentUserUseCase.execute(authentication)).thenReturn(dto);

        assertThat(controller.me(authentication)).isSameAs(dto);
        verify(getCurrentUserUseCase).execute(authentication);
    }
}
