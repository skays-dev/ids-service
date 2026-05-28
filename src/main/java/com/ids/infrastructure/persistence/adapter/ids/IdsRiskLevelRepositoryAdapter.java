package com.ids.infrastructure.persistence.adapter.ids;

import com.ids.domain.entity.ids.IdsRiskLevel;
import com.ids.domain.repository.ids.IdsRiskLevelRepository;
import com.ids.infrastructure.persistence.mapper.ids.IdsRiskLevelPersistenceMapper;
import com.ids.infrastructure.persistence.repository.ids.IdsRiskLevelJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class IdsRiskLevelRepositoryAdapter implements IdsRiskLevelRepository {

    private static final IdsRiskLevelPersistenceMapper.IdsRiskLevelPersistenceMapStructMapper MAPPER =
            IdsRiskLevelPersistenceMapper.IdsRiskLevelPersistenceMapStructMapper.INSTANCE;

    private final IdsRiskLevelJpaRepository idsRiskLevelJpaRepository;

    @Override
    public Optional<IdsRiskLevel> findByCode(String code) {
        return idsRiskLevelJpaRepository.findByCode(code)
                .map(MAPPER::toDomain);
    }

    @Override
    public List<IdsRiskLevel> findAllBySeverityOrderDesc() {
        return idsRiskLevelJpaRepository.findAllByOrderBySeverityOrderDesc().stream()
                .map(MAPPER::toDomain)
                .toList();
    }
}
