package com.ids.domain.service;

import com.ids.domain.entity.ids.IdsRiskLevel;
import com.ids.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.ids.testsupport.IdsTestFixtures.risk;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RiskClassificationServiceTest {

    private final RiskClassificationService service = new RiskClassificationService();

    @Test
    void classify_shouldReturnFirstRiskThatAcceptsConfidence() {
        IdsRiskLevel low = risk(1L, "LOW", new BigDecimal("0.00"), new BigDecimal("49.99"), 1);
        IdsRiskLevel high = risk(2L, "HIGH", new BigDecimal("80.00"), new BigDecimal("100.00"), 3);

        IdsRiskLevel result = service.classify(new BigDecimal("90.00"), List.of(low, high));

        assertThat(result).isSameAs(high);
    }

    @Test
    void classify_shouldThrowDomainExceptionWhenNoRiskMatches() {
        IdsRiskLevel low = risk(1L, "LOW", new BigDecimal("0.00"), new BigDecimal("49.99"), 1);

        assertThatThrownBy(() -> service.classify(new BigDecimal("70.00"), List.of(low)))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("No risk level found for confidence");
    }
}
