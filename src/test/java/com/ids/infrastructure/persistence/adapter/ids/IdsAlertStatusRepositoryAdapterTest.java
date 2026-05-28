package com.ids.infrastructure.persistence.adapter.ids;

import com.ids.domain.entity.ids.IdsAlertStatus;
import com.ids.infrastructure.persistence.repository.ids.IdsAlertStatusJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.ids.testsupport.IdsTestFixtures.statusJpa;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdsAlertStatusRepositoryAdapterTest {

    @Mock
    private IdsAlertStatusJpaRepository idsAlertStatusJpaRepository;

    @InjectMocks
    private IdsAlertStatusRepositoryAdapter adapter;

    @Test
    void findByCode_shouldReturnMappedStatusWhenFound() {
        when(idsAlertStatusJpaRepository.findByCode("NEW")).thenReturn(Optional.of(statusJpa(1L, "NEW")));

        Optional<IdsAlertStatus> result = adapter.findByCode("NEW");

        assertThat(result).isPresent();
        assertThat(result.get().getCode()).isEqualTo("NEW");
        verify(idsAlertStatusJpaRepository).findByCode("NEW");
    }

    @Test
    void findByCode_shouldReturnEmptyWhenNotFound() {
        when(idsAlertStatusJpaRepository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThat(adapter.findByCode("UNKNOWN")).isEmpty();
    }
}
