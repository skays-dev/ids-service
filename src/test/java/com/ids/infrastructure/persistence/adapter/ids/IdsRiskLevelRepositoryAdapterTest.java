package com.ids.infrastructure.persistence.adapter.ids;

import com.ids.domain.entity.ids.IdsRiskLevel;
import com.ids.infrastructure.persistence.repository.ids.IdsRiskLevelJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.ids.testsupport.IdsTestFixtures.riskJpa;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdsRiskLevelRepositoryAdapterTest {

    @Mock
    private IdsRiskLevelJpaRepository idsRiskLevelJpaRepository;

    @InjectMocks
    private IdsRiskLevelRepositoryAdapter adapter;

    @Test
    void findByCode_shouldReturnMappedRiskLevelWhenFound() {
        when(idsRiskLevelJpaRepository.findByCode("HIGH")).thenReturn(Optional.of(riskJpa(1L, "HIGH")));

        Optional<IdsRiskLevel> result = adapter.findByCode("HIGH");

        assertThat(result).isPresent();
        assertThat(result.get().getCode()).isEqualTo("HIGH");
        verify(idsRiskLevelJpaRepository).findByCode("HIGH");
    }

    @Test
    void findByCode_shouldReturnEmptyWhenNotFound() {
        when(idsRiskLevelJpaRepository.findByCode("HIGH")).thenReturn(Optional.empty());

        assertThat(adapter.findByCode("HIGH")).isEmpty();
    }

    @Test
    void findAllBySeverityOrderDesc_shouldMapRepositoryRows() {
        when(idsRiskLevelJpaRepository.findAllByOrderBySeverityOrderDesc())
                .thenReturn(List.of(riskJpa(2L, "CRITICAL"), riskJpa(1L, "HIGH")));

        List<IdsRiskLevel> result = adapter.findAllBySeverityOrderDesc();

        assertThat(result).extracting(IdsRiskLevel::getCode).containsExactly("CRITICAL", "HIGH");
        verify(idsRiskLevelJpaRepository).findAllByOrderBySeverityOrderDesc();
    }
}
