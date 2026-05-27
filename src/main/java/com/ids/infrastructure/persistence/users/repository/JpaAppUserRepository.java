package com.ids.infrastructure.persistence.users.repository;

import com.ids.infrastructure.persistence.users.entity.AppUserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaAppUserRepository extends JpaRepository<AppUserJpaEntity, Long> {
    Optional<AppUserJpaEntity> findByUsernameIgnoreCase(String username);
    boolean existsByUsernameIgnoreCase(String username);
}
