package com.ids.infrastructure.persistence.users.repository;

import com.ids.infrastructure.persistence.users.entity.AppRoleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaAppRoleRepository extends JpaRepository<AppRoleJpaEntity, Long> {
    Optional<AppRoleJpaEntity> findByName(String name);
}
