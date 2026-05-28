package com.ids.shared.mapper;

public abstract class AbstractDtoMapper<TDto, TEntity>
        extends AbstractMapper<TDto, TEntity>
        implements DtoMapper<TDto, TEntity> {

    @Override
    protected String getToTargetMethodName() {
        return "toDto";
    }

    @Override
    protected String getToSourceMethodName() {
        return "toEntity";
    }

    @Override
    public TDto toDto(TEntity source) {
        return mapToTarget(source);
    }

    @Override
    public TEntity toEntity(TDto source) {
        return mapToSource(source);
    }

    @Override
    public TDto toDtoFull(TEntity source) {
        return mapToTargetFull(source);
    }

    @Override
    public TEntity toEntityFull(TDto source) {
        return mapToSourceFull(source);
    }

    @Override
    public void addDozerConfiguration() {
    }
}