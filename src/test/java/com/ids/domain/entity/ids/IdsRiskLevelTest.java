package com.ids.domain.entity.ids;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class IdsRiskLevelTest {

    @Test
    void accepts_shouldReturnTrueWhenConfidenceIsInsideInclusiveBounds() {
        IdsRiskLevel risk = new IdsRiskLevel(1L, "HIGH", "High", new BigDecimal("80.00"), new BigDecimal("100.00"), 3);

        assertThat(risk.accepts(new BigDecimal("80.00"))).isTrue();
        assertThat(risk.accepts(new BigDecimal("90.50"))).isTrue();
        assertThat(risk.accepts(new BigDecimal("100.00"))).isTrue();
    }

    @Test
    void accepts_shouldReturnFalseWhenConfidenceIsOutsideBoundsOrMissing() {
        IdsRiskLevel risk = new IdsRiskLevel(1L, "HIGH", "High", new BigDecimal("80.00"), new BigDecimal("100.00"), 3);

        assertThat(risk.accepts(null)).isFalse();
        assertThat(risk.accepts(new BigDecimal("79.99"))).isFalse();
        assertThat(risk.accepts(new BigDecimal("100.01"))).isFalse();
    }

    @Test
    void accepts_shouldAllowOpenUpperBoundWhenMaxConfidenceIsNull() {
        IdsRiskLevel risk = new IdsRiskLevel(1L, "CRITICAL", "Critical", new BigDecimal("95.00"), null, 4);

        assertThat(risk.accepts(new BigDecimal("150.00"))).isTrue();
    }
}
