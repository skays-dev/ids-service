package com.ids.infrastructure.security;

import com.ids.domain.repository.usr.UsrUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.ids.testsupport.IdsTestFixtures.role;
import static com.ids.testsupport.IdsTestFixtures.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DatabaseUserDetailsServiceTest {

    @Mock
    private UsrUserRepository userRepository;

    @InjectMocks
    private DatabaseUserDetailsService service;

    @Test
    void loadUserByUsername_shouldReturnSpringSecurityUserDetails() {
        when(userRepository.findByUsernameIgnoreCase("admin"))
                .thenReturn(Optional.of(user(1L, "admin", true, role(1L, "ROLE_ADMIN"), role(2L, "ROLE_USER"))));

        var result = service.loadUserByUsername("admin");

        assertThat(result.getUsername()).isEqualTo("admin");
        assertThat(result.getPassword()).isEqualTo("encoded-password");
        assertThat(result.isEnabled()).isTrue();
        assertThat(result.getAuthorities()).extracting("authority").containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");
    }

    @Test
    void loadUserByUsername_shouldMarkDisabledUsersAsDisabled() {
        when(userRepository.findByUsernameIgnoreCase("blocked"))
                .thenReturn(Optional.of(user(1L, "blocked", false, role(1L, "ROLE_USER"))));

        assertThat(service.loadUserByUsername("blocked").isEnabled()).isFalse();
    }

    @Test
    void loadUserByUsername_shouldThrowWhenUserDoesNotExist() {
        when(userRepository.findByUsernameIgnoreCase("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("ghost"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found: ghost");
    }
}
