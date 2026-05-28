package com.ids.application.usecase.ids;

import com.ids.application.dto.ids.IdsAlertDto;
import com.ids.application.mapper.IdsAlertDtoMapper;
import com.ids.application.service.ids.IdsAlertRealtimePublisher;
import com.ids.domain.entity.ids.IdsAlert;
import com.ids.domain.entity.ids.IdsAlertStatus;
import com.ids.domain.exception.ResourceNotFoundException;
import com.ids.domain.repository.ids.IdsAlertRepository;
import com.ids.domain.repository.ids.IdsAlertStatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.ids.testsupport.IdsTestFixtures.alert;
import static com.ids.testsupport.IdsTestFixtures.alertDto;
import static com.ids.testsupport.IdsTestFixtures.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateIdsAlertStatusUseCaseTest {

    @Mock
    private IdsAlertRepository idsAlertRepository;

    @Mock
    private IdsAlertStatusRepository idsAlertStatusRepository;

    @Mock
    private IdsAlertRealtimePublisher realtimePublisher;

    @Mock
    private IdsAlertDtoMapper idsAlertDtoMapper;

    @InjectMocks
    private UpdateIdsAlertStatusUseCase useCase;

    @Test
    void execute_shouldChangeStatusSaveAndPublishUpdatedDto() {
        IdsAlert alert = alert(1L, "HIGH", "NEW");
        IdsAlertStatus closed = status(2L, "CLOSED");
        IdsAlertDto dto = alertDto(1L, "HIGH", "CLOSED");
        when(idsAlertRepository.findById(1L)).thenReturn(Optional.of(alert));
        when(idsAlertStatusRepository.findByCode("CLOSED")).thenReturn(Optional.of(closed));
        when(idsAlertRepository.save(alert)).thenReturn(alert);
        when(idsAlertDtoMapper.toDto(alert)).thenReturn(dto);

        IdsAlertDto result = useCase.execute(1L, " closed ");

        assertThat(result).isSameAs(dto);
        assertThat(alert.getStatus()).isSameAs(closed);
        InOrder inOrder = inOrder(idsAlertRepository, idsAlertDtoMapper, realtimePublisher);
        inOrder.verify(idsAlertRepository).save(alert);
        inOrder.verify(idsAlertDtoMapper).toDto(alert);
        inOrder.verify(realtimePublisher).publish(dto);
    }

    @Test
    void execute_shouldThrowWhenAlertDoesNotExist() {
        when(idsAlertRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(404L, "CLOSED"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("IdsAlert not found: 404");

        verify(idsAlertRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void execute_shouldThrowWhenStatusDoesNotExist() {
        when(idsAlertRepository.findById(1L)).thenReturn(Optional.of(alert(1L, "HIGH", "NEW")));
        when(idsAlertStatusRepository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(1L, "unknown"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Unknown status: unknown");
    }
}
