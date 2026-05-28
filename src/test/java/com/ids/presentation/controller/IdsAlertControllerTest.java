package com.ids.presentation.controller;

import com.ids.application.dto.ids.IdsAlertDto;
import com.ids.application.dto.ids.IdsAlertStatsDto;
import com.ids.application.usecase.ids.FindIdsAlertsUseCase;
import com.ids.application.usecase.ids.GetIdsAlertStatsUseCase;
import com.ids.application.usecase.ids.UpdateIdsAlertStatusUseCase;
import com.ids.domain.model.IdsAlertSearchCriteria;
import com.ids.domain.model.PageQuery;
import com.ids.domain.model.PageResult;
import com.ids.presentation.request.StatusUpdateRequest;
import com.ids.presentation.response.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static com.ids.testsupport.IdsTestFixtures.alertDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdsAlertControllerTest {

    @Mock
    private FindIdsAlertsUseCase findIdsAlertsUseCase;

    @Mock
    private GetIdsAlertStatsUseCase getIdsAlertStatsUseCase;

    @Mock
    private UpdateIdsAlertStatusUseCase updateIdsAlertStatusUseCase;

    @InjectMocks
    private IdsAlertController controller;

    @Test
    void idsAlerts_shouldBuildCriteriaAndReturnPageResponse() {
        IdsAlertDto dto = alertDto(1L, "HIGH", "NEW");
        when(findIdsAlertsUseCase.execute(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
                .thenReturn(new PageResult<>(List.of(dto), 0, 20, 1, 1, true, true));

        PageResponse<IdsAlertDto> result = controller.idsAlerts("NEW", "HIGH", "sql", 0, 20);

        assertThat(result.content()).containsExactly(dto);
        assertThat(result.totalElements()).isEqualTo(1);
        ArgumentCaptor<IdsAlertSearchCriteria> criteriaCaptor = ArgumentCaptor.forClass(IdsAlertSearchCriteria.class);
        ArgumentCaptor<PageQuery> pageQueryCaptor = ArgumentCaptor.forClass(PageQuery.class);
        verify(findIdsAlertsUseCase).execute(criteriaCaptor.capture(), pageQueryCaptor.capture());
        assertThat(criteriaCaptor.getValue().statusCode()).isEqualTo("NEW");
        assertThat(criteriaCaptor.getValue().riskCode()).isEqualTo("HIGH");
        assertThat(criteriaCaptor.getValue().search()).isEqualTo("sql");
        assertThat(pageQueryCaptor.getValue().sortBy()).isEqualTo("time");
        assertThat(pageQueryCaptor.getValue().direction()).isEqualTo(PageQuery.SortDirection.DESC);
    }

    @Test
    void stats_shouldDelegateToStatsUseCase() {
        IdsAlertStatsDto stats = new IdsAlertStatsDto(10, 2, 3, 1, Map.of("HIGH", 1L), Map.of("NEW", 3L));
        when(getIdsAlertStatsUseCase.execute()).thenReturn(stats);

        assertThat(controller.stats()).isSameAs(stats);
        verify(getIdsAlertStatsUseCase).execute();
    }

    @Test
    void updateStatus_shouldDelegateToUpdateUseCase() {
        IdsAlertDto dto = alertDto(1L, "HIGH", "CLOSED");
        when(updateIdsAlertStatusUseCase.execute(1L, "CLOSED")).thenReturn(dto);

        assertThat(controller.updateStatus(1L, new StatusUpdateRequest("CLOSED"))).isSameAs(dto);
        verify(updateIdsAlertStatusUseCase).execute(1L, "CLOSED");
    }
}
