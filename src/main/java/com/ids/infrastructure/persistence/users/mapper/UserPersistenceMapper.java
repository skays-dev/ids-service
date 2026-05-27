package com.ids.infrastructure.persistence.users.mapper;

import com.ids.domain.entity.AppRole;
import com.ids.domain.entity.AppUser;
import com.ids.infrastructure.persistence.users.entity.AppRoleJpaEntity;
import com.ids.infrastructure.persistence.users.entity.AppUserJpaEntity;

import java.util.stream.Collectors;

public final class UserPersistenceMapper {
    private UserPersistenceMapper() {}

    public static AppUser toDomain(AppUserJpaEntity entity) {
        if (entity == null) return null;
        return new AppUser(
                entity.getId(), entity.getUsername(), entity.getEmail(), entity.getPassword(), entity.isEnabled(),
                entity.getCreatedAt(), entity.getUpdatedAt(),
                entity.getRoles().stream().map(UserPersistenceMapper::toDomain).collect(Collectors.toSet())
        );
    }

    public static AppRole toDomain(AppRoleJpaEntity entity) {
        if (entity == null) return null;
        return new AppRole(entity.getId(), entity.getName(), entity.getDescription());
    }
}
