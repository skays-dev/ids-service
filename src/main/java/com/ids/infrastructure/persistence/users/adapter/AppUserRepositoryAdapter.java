package com.ids.infrastructure.persistence.users.adapter;

import com.ids.domain.entity.AppUser;
import com.ids.domain.repository.AppUserRepository;
import com.ids.infrastructure.persistence.users.mapper.UserPersistenceMapper;
import com.ids.infrastructure.persistence.users.repository.JpaAppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AppUserRepositoryAdapter implements AppUserRepository {
    private final JpaAppUserRepository jpaAppUserRepository;

    @Override
    public Optional<AppUser> findByUsernameIgnoreCase(String username) {
        return jpaAppUserRepository.findByUsernameIgnoreCase(username).map(UserPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByUsernameIgnoreCase(String username) {
        return jpaAppUserRepository.existsByUsernameIgnoreCase(username);
    }
}
