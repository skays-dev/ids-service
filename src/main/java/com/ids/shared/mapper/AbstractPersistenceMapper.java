package com.ids.shared.mapper;

public abstract class AbstractPersistenceMapper<TDomain, TJpaEntity>
        extends AbstractMapper<TDomain, TJpaEntity>
        implements PersistenceMapper<TDomain, TJpaEntity> {

    @Override
    protected String getToTargetMethodName() {
        return "toDomain";
    }

    @Override
    protected String getToSourceMethodName() {
        return "toEntity";
    }

    @Override
    public TDomain toDomain(TJpaEntity source) {
        return mapToTarget(source);
    }

    @Override
    public TJpaEntity toEntity(TDomain source) {
        return mapToSource(source);
    }

    @Override
    public TDomain toDomainFull(TJpaEntity source) {
        return mapToTargetFull(source);
    }

    @Override
    public TJpaEntity toEntityFull(TDomain source) {
        return mapToSourceFull(source);
    }
}