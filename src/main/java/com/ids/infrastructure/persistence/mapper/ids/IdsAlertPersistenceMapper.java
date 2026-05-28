package com.ids.infrastructure.persistence.mapper.ids;

import com.ids.domain.entity.ids.IdsAlert;
import com.ids.infrastructure.persistence.entity.ids.IdsAlertJpaEntity;
import com.ids.shared.mapper.AbstractPersistenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class IdsAlertPersistenceMapper
        extends AbstractPersistenceMapper<IdsAlert, IdsAlertJpaEntity> {

    @Mapper(uses = {
            IdsRiskLevelPersistenceMapper.IdsRiskLevelPersistenceMapStructMapper.class,
            IdsAlertStatusPersistenceMapper.IdsAlertStatusPersistenceMapStructMapper.class
    })
    public interface IdsAlertPersistenceMapStructMapper {

        IdsAlertPersistenceMapStructMapper INSTANCE =
                Mappers.getMapper(IdsAlertPersistenceMapStructMapper.class);

        @Mapping(target = "idsRiskLevel", source = "idsRiskLevel")
        @Mapping(target = "status", source = "status")
        IdsAlert toDomain(IdsAlertJpaEntity entity);

        @Mapping(target = "idsRiskLevel", source = "idsRiskLevel")
        @Mapping(target = "status", source = "status")
        IdsAlertJpaEntity toEntity(IdsAlert domain);
    }

    @Override
    public void addConfiguration() {
    }
}