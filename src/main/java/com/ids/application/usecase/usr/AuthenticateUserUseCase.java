package com.ids.application.usecase.usr;

import com.ids.application.dto.usr.AuthResult;
import com.ids.application.service.usr.TokenProvider;
import com.ids.domain.entity.usr.UsrUser;
import com.ids.domain.repository.usr.UsrUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticateUserUseCase {
    private final AuthenticationManager authenticationManager;
    private final UsrUserRepository usrUserRepository;
    private final TokenProvider tokenProvider;

    @Transactional(readOnly = true, transactionManager = "usersTransactionManager")
    public AuthResult execute(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UsrUser user = usrUserRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found after authentication"));
        TokenProvider.TokenResult token = tokenProvider.createToken(user);
        return new AuthResult(token.token(), "Bearer", token.expiresAt(), user.getUsername(), token.roles());
    }
}
