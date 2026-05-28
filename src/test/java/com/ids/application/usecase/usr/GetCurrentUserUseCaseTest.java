package com.ids.application.usecase.usr;

import com.ids.application.dto.usr.CurrentUserDto;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GetCurrentUserUseCaseTest {

    private final GetCurrentUserUseCase useCase = new GetCurrentUserUseCase();

    @Test
    void execute_shouldReturnUsernameAndSortedAuthorities() {
        var authentication = new UsernamePasswordAuthenticationToken(
                "admin",
                "n/a",
                List.of(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        CurrentUserDto result = useCase.execute(authentication);

        assertThat(result.username()).isEqualTo("admin");
        assertThat(result.roles()).containsExactly("ROLE_ADMIN", "ROLE_USER");
    }
}
