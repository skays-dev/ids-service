package com.ids.application.usecase.ids;

import com.ids.application.dto.ids.IdsAlertDto;
import com.ids.application.mapper.IdsAlertDtoMapper;
import com.ids.domain.entity.ids.IdsAlert;
import com.ids.domain.model.IdsAlertSearchCriteria;
import com.ids.domain.model.PageQuery;
import com.ids.domain.model.PageResult;
import com.ids.domain.repository.ids.IdsAlertRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.ids.testsupport.IdsTestFixtures.alert;
import static com.ids.testsupport.IdsTestFixtures.alertDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindIdsAlertsUseCaseTest {

    @Mock
    private IdsAlertRepository idsAlertRepository;

    @Mock
    private IdsAlertDtoMapper idsAlertDtoMapper;

    @InjectMocks
    private FindIdsAlertsUseCase useCase;

    @Test
    void execute_shouldSearchAndMapPageContentToDto() {
        IdsAlertSearchCriteria criteria = new IdsAlertSearchCriteria("NEW", "HIGH", "sql");
        PageQuery pageQuery = new PageQuery(0, 20, "time", PageQuery.SortDirection.DESC);
        IdsAlert alert = alert(1L, "HIGH", "NEW");
        IdsAlertDto dto = alertDto(1L, "HIGH", "NEW");
        when(idsAlertRepository.search(criteria, pageQuery))
                .thenReturn(new PageResult<>(List.of(alert), 0, 20, 1, 1, true, true));
        when(idsAlertDtoMapper.toDto(alert)).thenReturn(dto);

        PageResult<IdsAlertDto> result = useCase.execute(criteria, pageQuery);

        assertThat(result.content()).containsExactly(dto);
        assertThat(result.totalElements()).isEqualTo(1);
        verify(idsAlertRepository).search(criteria, pageQuery);
        verify(idsAlertDtoMapper).toDto(alert);
    }
}
