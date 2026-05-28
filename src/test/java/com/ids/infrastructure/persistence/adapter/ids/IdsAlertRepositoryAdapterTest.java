package com.ids.infrastructure.persistence.adapter.ids;

import com.ids.domain.entity.ids.IdsAlert;
import com.ids.domain.model.IdsAlertSearchCriteria;
import com.ids.domain.model.PageQuery;
import com.ids.domain.model.PageResult;
import com.ids.infrastructure.persistence.entity.ids.IdsAlertJpaEntity;
import com.ids.infrastructure.persistence.entity.ids.IdsAlertStatusJpaEntity;
import com.ids.infrastructure.persistence.entity.ids.IdsRiskLevelJpaEntity;
import com.ids.infrastructure.persistence.repository.ids.IdsAlertJpaRepository;
import com.ids.infrastructure.persistence.repository.ids.IdsAlertStatusJpaRepository;
import com.ids.infrastructure.persistence.repository.ids.IdsRiskLevelJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ids.testsupport.IdsTestFixtures.alert;
import static com.ids.testsupport.IdsTestFixtures.alertJpa;
import static com.ids.testsupport.IdsTestFixtures.riskJpa;
import static com.ids.testsupport.IdsTestFixtures.statusJpa;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdsAlertRepositoryAdapterTest {

    @Mock
    private IdsAlertJpaRepository idsAlertJpaRepository;

    @Mock
    private IdsAlertStatusJpaRepository idsAlertStatusJpaRepository;

    @Mock
    private IdsRiskLevelJpaRepository idsRiskLevelJpaRepository;

    @InjectMocks
    private IdsAlertRepositoryAdapter adapter;

    @Test
    void search_shouldDelegateToJpaRepositoryAndMapPageResult() {
        IdsAlertJpaEntity entity = alertJpa(1L, "HIGH", "NEW");
        when(idsAlertJpaRepository.findAll(anySpecification(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(entity)));

        PageResult<IdsAlert> result = adapter.search(
                new IdsAlertSearchCriteria("NEW", "HIGH", "sql"),
                new PageQuery(0, 20, "confidence", PageQuery.SortDirection.ASC)
        );

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0).getId()).isEqualTo(1L);
        assertThat(result.content().get(0).getIdsRiskLevel().getCode()).isEqualTo("HIGH");
        assertThat(result.content().get(0).getStatus().getCode()).isEqualTo("NEW");

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(idsAlertJpaRepository).findAll(anySpecification(), pageableCaptor.capture());
        assertThat(pageableCaptor.getValue().getPageNumber()).isZero();
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(20);
        assertThat(pageableCaptor.getValue().getSort().getOrderFor("confidence")).isNotNull();
        assertThat(pageableCaptor.getValue().getSort().getOrderFor("confidence").isAscending()).isTrue();
    }

    @Test
    void findById_shouldReturnMappedDomainWhenEntityExists() {
        when(idsAlertJpaRepository.findById(1L)).thenReturn(Optional.of(alertJpa(1L, "HIGH", "NEW")));

        Optional<IdsAlert> result = adapter.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getIdsRiskLevel().getCode()).isEqualTo("HIGH");
        verify(idsAlertJpaRepository).findById(1L);
    }

    @Test
    void save_shouldReplaceNestedObjectsWithJpaReferencesBeforeSaving() {
        IdsAlert alert = alert(1L, "HIGH", "NEW");
        IdsRiskLevelJpaEntity riskReference = riskJpa(11L, "HIGH");
        IdsAlertStatusJpaEntity statusReference = statusJpa(21L, "NEW");
        when(idsRiskLevelJpaRepository.getReferenceById(11L)).thenReturn(riskReference);
        when(idsAlertStatusJpaRepository.getReferenceById(21L)).thenReturn(statusReference);
        when(idsAlertJpaRepository.save(any(IdsAlertJpaEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IdsAlert result = adapter.save(alert);

        ArgumentCaptor<IdsAlertJpaEntity> entityCaptor = ArgumentCaptor.forClass(IdsAlertJpaEntity.class);
        verify(idsAlertJpaRepository).save(entityCaptor.capture());
        assertThat(entityCaptor.getValue().getIdsRiskLevel()).isSameAs(riskReference);
        assertThat(entityCaptor.getValue().getStatus()).isSameAs(statusReference);
        assertThat(result.getIdsRiskLevel().getCode()).isEqualTo("HIGH");
        assertThat(result.getStatus().getCode()).isEqualTo("NEW");
    }

    @Test
    void simpleCounters_shouldDelegateToJpaRepository() {
        LocalDateTime from = LocalDateTime.of(2026, 5, 28, 10, 30);
        when(idsAlertJpaRepository.existsByEventHash("hash-001")).thenReturn(true);
        when(idsAlertJpaRepository.count()).thenReturn(15L);
        when(idsAlertJpaRepository.countSince(from)).thenReturn(7L);
        when(idsAlertJpaRepository.countByStatusCode("NEW")).thenReturn(6L);
        when(idsAlertJpaRepository.countByRiskAndStatus("HIGH", "NEW")).thenReturn(3L);
        List<Object[]> byRisk = (List<Object[]>) List.of(new Object[]{"HIGH", 3L});
        List<Object[]> byStatus = (List<Object[]>) List.of(new Object[]{"NEW", 6L});
        when(idsAlertJpaRepository.countByRisk()).thenReturn(byRisk);
        when(idsAlertJpaRepository.countByStatus()).thenReturn(byStatus);

        assertThat(adapter.existsByEventHash("hash-001")).isTrue();
        assertThat(adapter.count()).isEqualTo(15L);
        assertThat(adapter.countSince(from)).isEqualTo(7L);
        assertThat(adapter.countByStatusCode("NEW")).isEqualTo(6L);
        assertThat(adapter.countByRiskAndStatus("HIGH", "NEW")).isEqualTo(3L);
        assertThat(adapter.countByRisk()).isSameAs(byRisk);
        assertThat(adapter.countByStatus()).isSameAs(byStatus);
    }

    @Test
    void realtimeFingerprint_shouldReturnDefaultWhenNoRowsExist() {
        when(idsAlertJpaRepository.realtimeFingerprintParts()).thenReturn(null);
        assertThat(adapter.realtimeFingerprint()).isEqualTo("0|");

        when(idsAlertJpaRepository.realtimeFingerprintParts()).thenReturn(Collections.emptyList());
        assertThat(adapter.realtimeFingerprint()).isEqualTo("0|");
    }

    @Test
    void realtimeFingerprint_shouldBuildFingerprintFromFirstRow() {
        when(idsAlertJpaRepository.realtimeFingerprintParts())
                .thenReturn((List<Object[]>) List.of(new Object[]{12L, "2026-05-28T10:30:00"}));

        assertThat(adapter.realtimeFingerprint()).isEqualTo("12|2026-05-28T10:30:00");
    }

    @SuppressWarnings("unchecked")
    private Specification<IdsAlertJpaEntity> anySpecification() {
        return any(Specification.class);
    }
}
