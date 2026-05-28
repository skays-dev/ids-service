package com.ids.domain.entity.ids;

import com.ids.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.ids.testsupport.IdsTestFixtures.risk;
import static com.ids.testsupport.IdsTestFixtures.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IdsAlertTest {

    @Test
    void createNew_shouldCreateAlertWithRequiredFieldsAndCreationDate() {
        IdsRiskLevel risk = risk(1L, "HIGH", new BigDecimal("80.00"), new BigDecimal("100.00"), 3);
        IdsAlertStatus status = status(2L, "NEW");
        LocalDateTime eventTime = LocalDateTime.of(2026, 5, 28, 10, 30);

        IdsAlert alert = IdsAlert.createNew(
                eventTime,
                "reason",
                "attack",
                new BigDecimal("90.00"),
                "10.0.0.1",
                "10.0.0.2",
                1234,
                443,
                6,
                risk,
                status,
                "hash-001"
        );

        assertThat(alert.getTime()).isEqualTo(eventTime);
        assertThat(alert.getConfidence()).isEqualByComparingTo("90.00");
        assertThat(alert.getIdsRiskLevel()).isSameAs(risk);
        assertThat(alert.getStatus()).isSameAs(status);
        assertThat(alert.getEventHash()).isEqualTo("hash-001");
        assertThat(alert.getCreatedAt()).isNotNull();
    }

    @Test
    void createNew_shouldRejectMissingRequiredValues() {
        assertThatThrownBy(() -> IdsAlert.createNew(null, null, null, BigDecimal.ONE, null, null, null, null, null, risk(1L, "HIGH", BigDecimal.ZERO, BigDecimal.TEN, 1), status(1L, "NEW"), "hash"))
                .isInstanceOf(DomainException.class)
                .hasMessage("IdsAlert time is required");

        assertThatThrownBy(() -> IdsAlert.createNew(LocalDateTime.now(), null, null, null, null, null, null, null, null, risk(1L, "HIGH", BigDecimal.ZERO, BigDecimal.TEN, 1), status(1L, "NEW"), "hash"))
                .isInstanceOf(DomainException.class)
                .hasMessage("Confidence is required");
    }

    @Test
    void changeStatus_shouldUpdateStatusAndUpdatedAt() {
        IdsAlert alert = new IdsAlert();
        IdsAlertStatus newStatus = status(3L, "CLOSED");

        alert.changeStatus(newStatus);

        assertThat(alert.getStatus()).isSameAs(newStatus);
        assertThat(alert.getUpdatedAt()).isNotNull();
    }

    @Test
    void changeStatus_shouldRejectNullStatus() {
        IdsAlert alert = new IdsAlert();

        assertThatThrownBy(() -> alert.changeStatus(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("IdsAlert status is required");
    }
}
