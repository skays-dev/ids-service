package com.ids.application.mapper;

import com.ids.application.dto.ids.IdsAlertDto;
import com.ids.domain.entity.ids.IdsAlert;
import org.junit.jupiter.api.Test;

import static com.ids.testsupport.IdsTestFixtures.alert;
import static com.ids.testsupport.IdsTestFixtures.alertDto;
import static org.assertj.core.api.Assertions.assertThat;

class IdsAlertDtoMapperTest {

    @Test
    void toDto_shouldFlattenRiskAndStatusFields() {
        IdsAlert domain = alert(1L, "HIGH", "NEW");

        IdsAlertDto dto = IdsAlertDtoMapper.IdsAlertDtoMapStructMapper.INSTANCE.toDto(domain);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.riskCode()).isEqualTo("HIGH");
        assertThat(dto.riskLabel()).isEqualTo("HIGH label");
        assertThat(dto.statusCode()).isEqualTo("NEW");
        assertThat(dto.statusLabel()).isEqualTo("NEW label");
    }

    @Test
    void toEntity_shouldCreateNestedRiskAndStatusFromDto() {
        IdsAlertDto dto = alertDto(1L, "HIGH", "NEW");

        IdsAlert domain = IdsAlertDtoMapper.IdsAlertDtoMapStructMapper.INSTANCE.toEntity(dto);

        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getIdsRiskLevel().getCode()).isEqualTo("HIGH");
        assertThat(domain.getIdsRiskLevel().getLabel()).isEqualTo("HIGH label");
        assertThat(domain.getStatus().getCode()).isEqualTo("NEW");
        assertThat(domain.getStatus().getLabel()).isEqualTo("NEW label");
    }
}
