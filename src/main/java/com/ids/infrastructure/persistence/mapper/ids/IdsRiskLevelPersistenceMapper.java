package com.ids.infrastructure.persistence.mapper.ids;

import com.ids.domain.entity.ids.IdsRiskLevel;
import com.ids.infrastructure.persistence.entity.ids.IdsRiskLevelJpaEntity;
import com.ids.shared.mapper.AbstractPersistenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class IdsRiskLevelPersistenceMapper
        extends AbstractPersistenceMapper<IdsRiskLevel, IdsRiskLevelJpaEntity> {

    @Mapper
    public interface IdsRiskLevelPersistenceMapStructMapper {

        IdsRiskLevelPersistenceMapStructMapper INSTANCE =
                Mappers.getMapper(IdsRiskLevelPersistenceMapStructMapper.class);

        IdsRiskLevel toDomain(IdsRiskLevelJpaEntity entity);

        IdsRiskLevelJpaEntity toEntity(IdsRiskLevel domain);
    }

    @Override
    public void addConfiguration() {
    }
}