package com.ids.application.usecase.ids;

import com.ids.application.dto.ids.IdsAlertStatsDto;
import com.ids.domain.repository.ids.IdsAlertRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetIdsAlertStatsUseCaseTest {

    @Mock
    private IdsAlertRepository idsAlertRepository;

    @InjectMocks
    private GetIdsAlertStatsUseCase useCase;

    @Test
    void execute_shouldAggregateCountersAndGroupedRows() {
        when(idsAlertRepository.countByRisk()).thenReturn(List.of(new Object[]{"HIGH", 3L}, new Object[]{"LOW", 2L}));
        when(idsAlertRepository.countByStatus()).thenReturn(List.of(new Object[]{"NEW", 4L}, new Object[]{"CLOSED", 1L}));
        when(idsAlertRepository.count()).thenReturn(5L);
        when(idsAlertRepository.countSince(any(LocalDateTime.class))).thenReturn(2L);
        when(idsAlertRepository.countByStatusCode("NEW")).thenReturn(4L);
        when(idsAlertRepository.countByRiskAndStatus("HIGH", "NEW")).thenReturn(3L);

        IdsAlertStatsDto result = useCase.execute();

        assertThat(result.total()).isEqualTo(5L);
        assertThat(result.last24h()).isEqualTo(2L);
        assertThat(result.newIdsAlerts()).isEqualTo(4L);
        assertThat(result.highRiskNew()).isEqualTo(3L);
        assertThat(result.byRisk()).containsEntry("HIGH", 3L).containsEntry("LOW", 2L);
        assertThat(result.byStatus()).containsEntry("NEW", 4L).containsEntry("CLOSED", 1L);

        ArgumentCaptor<LocalDateTime> fromCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(idsAlertRepository).countSince(fromCaptor.capture());
        assertThat(fromCaptor.getValue()).isBeforeOrEqualTo(LocalDateTime.now());
    }
}
