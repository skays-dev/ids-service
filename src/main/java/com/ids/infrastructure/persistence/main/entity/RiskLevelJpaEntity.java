package com.ids.infrastructure.persistence.main.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "IDS_RISK_LEVEL", schema = "u835525338_IDS_main")
@Getter
@Setter
@NoArgsConstructor
public class RiskLevelJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODE", nullable = false, unique = true, length = 30)
    private String code;

    @Column(name = "LABEL", nullable = false, length = 80)
    private String label;

    @Column(name = "MIN_CONFIDENCE", nullable = false, precision = 5, scale = 2)
    private BigDecimal minConfidence;

    @Column(name = "MAX_CONFIDENCE", precision = 5, scale = 2)
    private BigDecimal maxConfidence;

    @Column(name = "SEVERITY_ORDER", nullable = false)
    private Integer severityOrder;
}
