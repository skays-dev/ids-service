package com.ids.infrastructure.persistence.adapter.usr;

import com.ids.domain.entity.usr.UsrUser;
import com.ids.infrastructure.persistence.repository.usr.UsrUserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.ids.testsupport.IdsTestFixtures.roleJpa;
import static com.ids.testsupport.IdsTestFixtures.userJpa;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsrUserRepositoryAdapterTest {

    @Mock
    private UsrUserJpaRepository usrUserJpaRepository;

    @InjectMocks
    private UsrUserRepositoryAdapter adapter;

    @Test
    void findByUsernameIgnoreCase_shouldReturnMappedUserWithRolesWhenFound() {
        when(usrUserJpaRepository.findByUsernameIgnoreCase("admin"))
                .thenReturn(Optional.of(userJpa(1L, "admin", true, roleJpa(10L, "ROLE_ADMIN"))));

        Optional<UsrUser> result = adapter.findByUsernameIgnoreCase("admin");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("admin");
        assertThat(result.get().getRoles()).extracting("name").containsExactly("ROLE_ADMIN");
        verify(usrUserJpaRepository).findByUsernameIgnoreCase("admin");
    }

    @Test
    void findByUsernameIgnoreCase_shouldReturnEmptyWhenUserDoesNotExist() {
        when(usrUserJpaRepository.findByUsernameIgnoreCase("ghost")).thenReturn(Optional.empty());

        assertThat(adapter.findByUsernameIgnoreCase("ghost")).isEmpty();
    }

    @Test
    void existsByUsernameIgnoreCase_shouldDelegateToJpaRepository() {
        when(usrUserJpaRepository.existsByUsernameIgnoreCase("admin")).thenReturn(true);

        assertThat(adapter.existsByUsernameIgnoreCase("admin")).isTrue();
        verify(usrUserJpaRepository).existsByUsernameIgnoreCase("admin");
    }
}
