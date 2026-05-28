package com.ids.infrastructure.persistence.mapper.usr;

import com.ids.domain.entity.usr.UsrRole;
import com.ids.domain.entity.usr.UsrUser;
import com.ids.infrastructure.persistence.entity.usr.UsrRoleJpaEntity;
import com.ids.infrastructure.persistence.entity.usr.UsrUserJpaEntity;
import com.ids.shared.mapper.AbstractPersistenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper
        extends AbstractPersistenceMapper<UsrUser, UsrUserJpaEntity> {

    @Mapper(uses = {
            UserPersistenceMapper.UsrRolePersistenceMapStructMapper.class
    })
    public interface UserPersistenceMapStructMapper {

        UserPersistenceMapStructMapper INSTANCE =
                Mappers.getMapper(UserPersistenceMapStructMapper.class);

        @Mapping(target = "roles", source = "roles")
        UsrUser toDomain(UsrUserJpaEntity entity);

        @Mapping(target = "roles", source = "roles")
        UsrUserJpaEntity toEntity(UsrUser domain);
    }

    @Mapper
    public interface UsrRolePersistenceMapStructMapper {

        UsrRolePersistenceMapStructMapper INSTANCE =
                Mappers.getMapper(UsrRolePersistenceMapStructMapper.class);

        UsrRole toDomain(UsrRoleJpaEntity entity);

        UsrRoleJpaEntity toEntity(UsrRole domain);
    }

    @Override
    public void addConfiguration() {
        // MapStruct handles the mapping.
    }
}