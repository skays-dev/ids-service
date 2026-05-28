package com.ids.infrastructure.persistence.repository.ids;

import com.ids.infrastructure.persistence.entity.ids.IdsAlertStatusJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdsAlertStatusJpaRepository extends JpaRepository<IdsAlertStatusJpaEntity, Long> {
    Optional<IdsAlertStatusJpaEntity> findByCode(String code);
}
