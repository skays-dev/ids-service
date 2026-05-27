package com.ids.presentation.controller;

import com.ids.application.dto.AuthResult;
import com.ids.application.dto.CurrentUserDto;
import com.ids.application.usecase.AuthenticateUserUseCase;
import com.ids.application.usecase.GetCurrentUserUseCase;
import com.ids.presentation.request.LoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;

    @PostMapping("/api/auth/login")
    public AuthResult login(@Valid @RequestBody LoginRequest request) {
        return authenticateUserUseCase.execute(request.username(), request.password());
    }

    @PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public AuthResult oauthToken(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam(defaultValue = "password") String grant_type) {
        if (!"password".equalsIgnoreCase(grant_type)) {
            throw new IllegalArgumentException("Only grant_type=password is enabled in this starter. Use Keycloak or another OAuth2 provider for Authorization Code + PKCE.");
        }
        return authenticateUserUseCase.execute(username, password);
    }

    @GetMapping("/api/auth/me")
    public CurrentUserDto me(Authentication authentication) {
        return getCurrentUserUseCase.execute(authentication);
    }
}
