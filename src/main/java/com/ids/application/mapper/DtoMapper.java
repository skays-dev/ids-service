package com.ids.application.mapper;

public interface DtoMapper<E, D> {
    D toDto(E entity);

    default E toEntity(D dto) {
        throw new UnsupportedOperationException("Reverse mapping is not implemented for this mapper");
    }
}
