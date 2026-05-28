package com.ids.shared.mapper;

import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;

public interface DtoMapper<TDto, TEntity> {

    @Nullable
    TDto toDto(@Nullable TEntity source);

    @Nullable
    TEntity toEntity(@Nullable TDto source);

    default List<TDto> toDtos(@Nullable Collection<TEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }

        return entities.stream()
                .map(this::toDto)
                .toList();
    }

    default List<TEntity> toEntities(@Nullable Collection<TDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return List.of();
        }

        return dtos.stream()
                .map(this::toEntity)
                .toList();
    }

    @Nullable
    TDto toDtoFull(@Nullable TEntity source);

    @Nullable
    TEntity toEntityFull(@Nullable TDto source);

    default List<TDto> toDtosFull(@Nullable Collection<TEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }

        return entities.stream()
                .map(this::toDtoFull)
                .toList();
    }

    default List<TEntity> toEntitiesFull(@Nullable Collection<TDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return List.of();
        }

        return dtos.stream()
                .map(this::toEntityFull)
                .toList();
    }

    default void addConfiguration() {
    }

    default void addConfigurationFull() {
    }

    @Deprecated
    default @Nullable TDto toDtoDozer(@Nullable TEntity source) {
        return toDto(source);
    }

    @Deprecated
    default List<TDto> toDtosDozer(@Nullable Collection<TEntity> entities) {
        return toDtos(entities);
    }

    @Deprecated
    default void addDozerConfiguration() {
    }
}