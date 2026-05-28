package com.ids.shared.mapper;

import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;

public interface PersistenceMapper<TDomain, TJpaEntity> {

    @Nullable
    TDomain toDomain(@Nullable TJpaEntity source);

    @Nullable
    TJpaEntity toEntity(@Nullable TDomain source);

    default List<TDomain> toDomains(@Nullable Collection<TJpaEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }

        return entities.stream()
                .map(this::toDomain)
                .toList();
    }

    default List<TJpaEntity> toEntities(@Nullable Collection<TDomain> domains) {
        if (domains == null || domains.isEmpty()) {
            return List.of();
        }

        return domains.stream()
                .map(this::toEntity)
                .toList();
    }

    @Nullable
    TDomain toDomainFull(@Nullable TJpaEntity source);

    @Nullable
    TJpaEntity toEntityFull(@Nullable TDomain source);

    default List<TDomain> toDomainsFull(@Nullable Collection<TJpaEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }

        return entities.stream()
                .map(this::toDomainFull)
                .toList();
    }

    default List<TJpaEntity> toEntitiesFull(@Nullable Collection<TDomain> domains) {
        if (domains == null || domains.isEmpty()) {
            return List.of();
        }

        return domains.stream()
                .map(this::toEntityFull)
                .toList();
    }

    default void addConfiguration() {
    }

    default void addConfigurationFull() {
    }
}