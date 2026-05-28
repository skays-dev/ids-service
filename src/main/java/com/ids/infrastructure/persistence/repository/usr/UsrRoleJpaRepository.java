package com.ids.infrastructure.persistence.repository.usr;

import com.ids.infrastructure.persistence.entity.usr.UsrRoleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsrRoleJpaRepository extends JpaRepository<UsrRoleJpaEntity, Long> {
    Optional<UsrRoleJpaEntity> findByName(String name);
}
