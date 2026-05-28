package com.ids.domain.entity.ids;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdsRiskLevel {
    private Long id;
    private String code;
    private String label;
    private BigDecimal minConfidence;
    private BigDecimal maxConfidence;
    private Integer severityOrder;

    public boolean accepts(BigDecimal confidence) {
        if (confidence == null || minConfidence == null) {
            return false;
        }
        boolean minOk = confidence.compareTo(minConfidence) >= 0;
        boolean maxOk = maxConfidence == null || confidence.compareTo(maxConfidence) <= 0;
        return minOk && maxOk;
    }
}
