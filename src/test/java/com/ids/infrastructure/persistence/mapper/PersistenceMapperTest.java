package com.ids.infrastructure.persistence.mapper;

import com.ids.domain.entity.ids.IdsAlert;
import com.ids.domain.entity.usr.UsrUser;
import com.ids.infrastructure.persistence.entity.ids.IdsAlertJpaEntity;
import com.ids.infrastructure.persistence.entity.usr.UsrUserJpaEntity;
import com.ids.infrastructure.persistence.mapper.ids.IdsAlertPersistenceMapper;
import com.ids.infrastructure.persistence.mapper.ids.IdsAlertStatusPersistenceMapper;
import com.ids.infrastructure.persistence.mapper.ids.IdsRiskLevelPersistenceMapper;
import com.ids.infrastructure.persistence.mapper.usr.UserPersistenceMapper;
import org.junit.jupiter.api.Test;

import static com.ids.testsupport.IdsTestFixtures.alertJpa;
import static com.ids.testsupport.IdsTestFixtures.roleJpa;
import static com.ids.testsupport.IdsTestFixtures.userJpa;
import static org.assertj.core.api.Assertions.assertThat;

class PersistenceMapperTest {

    @Test
    void idsAlertMapper_shouldMapNestedRiskAndStatusToDomain() {
        IdsAlertJpaEntity entity = alertJpa(1L, "HIGH", "NEW");

        IdsAlert result = IdsAlertPersistenceMapper.IdsAlertPersistenceMapStructMapper.INSTANCE.toDomain(entity);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getIdsRiskLevel().getCode()).isEqualTo("HIGH");
        assertThat(result.getStatus().getCode()).isEqualTo("NEW");
        assertThat(result.getEventHash()).isEqualTo("hash-001");
    }

    @Test
    void idsAlertMapper_shouldMapNestedRiskAndStatusToJpaEntity() {
        IdsAlert domain = com.ids.testsupport.IdsTestFixtures.alert(1L, "HIGH", "NEW");

        IdsAlertJpaEntity result = IdsAlertPersistenceMapper.IdsAlertPersistenceMapStructMapper.INSTANCE.toEntity(domain);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getIdsRiskLevel().getCode()).isEqualTo("HIGH");
        assertThat(result.getStatus().getCode()).isEqualTo("NEW");
        assertThat(result.getEventHash()).isEqualTo("hash-001");
    }

    @Test
    void simplePersistenceMappers_shouldHandleNullValues() {
        assertThat(IdsAlertStatusPersistenceMapper.IdsAlertStatusPersistenceMapStructMapper.INSTANCE.toDomain(null)).isNull();
        assertThat(IdsAlertStatusPersistenceMapper.IdsAlertStatusPersistenceMapStructMapper.INSTANCE.toEntity(null)).isNull();
        assertThat(IdsRiskLevelPersistenceMapper.IdsRiskLevelPersistenceMapStructMapper.INSTANCE.toDomain(null)).isNull();
        assertThat(IdsRiskLevelPersistenceMapper.IdsRiskLevelPersistenceMapStructMapper.INSTANCE.toEntity(null)).isNull();
        assertThat(UserPersistenceMapper.UserPersistenceMapStructMapper.INSTANCE.toDomain(null)).isNull();
        assertThat(UserPersistenceMapper.UserPersistenceMapStructMapper.INSTANCE.toEntity(null)).isNull();
    }

    @Test
    void userMapper_shouldMapRolesInBothDirections() {
        UsrUserJpaEntity entity = userJpa(1L, "admin", true, roleJpa(10L, "ROLE_ADMIN"));

        UsrUser domain = UserPersistenceMapper.UserPersistenceMapStructMapper.INSTANCE.toDomain(entity);
        UsrUserJpaEntity mappedBack = UserPersistenceMapper.UserPersistenceMapStructMapper.INSTANCE.toEntity(domain);

        assertThat(domain.getUsername()).isEqualTo("admin");
        assertThat(domain.getRoles()).extracting("name").containsExactly("ROLE_ADMIN");
        assertThat(mappedBack.getRoles()).extracting("name").containsExactly("ROLE_ADMIN");
    }
}
