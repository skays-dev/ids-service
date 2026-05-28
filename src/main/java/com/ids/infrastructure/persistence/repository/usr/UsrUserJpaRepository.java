package com.ids.infrastructure.persistence.repository.usr;

import com.ids.infrastructure.persistence.entity.usr.UsrUserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsrUserJpaRepository extends JpaRepository<UsrUserJpaEntity, Long> {
    Optional<UsrUserJpaEntity> findByUsernameIgnoreCase(String username);
    boolean existsByUsernameIgnoreCase(String username);
}
