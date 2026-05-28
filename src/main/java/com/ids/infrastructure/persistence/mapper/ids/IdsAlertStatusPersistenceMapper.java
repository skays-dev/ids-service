package com.ids.infrastructure.persistence.mapper.ids;

import com.ids.domain.entity.ids.IdsAlertStatus;
import com.ids.infrastructure.persistence.entity.ids.IdsAlertStatusJpaEntity;
import com.ids.shared.mapper.AbstractPersistenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class IdsAlertStatusPersistenceMapper
        extends AbstractPersistenceMapper<IdsAlertStatus, IdsAlertStatusJpaEntity> {

    @Mapper
    public interface IdsAlertStatusPersistenceMapStructMapper {

        IdsAlertStatusPersistenceMapStructMapper INSTANCE =
                Mappers.getMapper(IdsAlertStatusPersistenceMapStructMapper.class);

        IdsAlertStatus toDomain(IdsAlertStatusJpaEntity entity);

        IdsAlertStatusJpaEntity toEntity(IdsAlertStatus domain);
    }

    @Override
    public void addConfiguration() {
    }
}