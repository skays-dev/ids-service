package com.ids.infrastructure.persistence.main.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "IDS_ALERT", indexes = {
        @Index(name = "idx_ids_alert_time", columnList = "EVENT_TIME"),
        @Index(name = "idx_ids_alert_hash", columnList = "EVENT_HASH", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
public class AlertJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "EVENT_TIME", nullable = false)
    private LocalDateTime time;

    @Column(name = "REASON", length = 255)
    private String reason;

    @Column(name = "ATTACK", length = 255)
    private String attack;

    @Column(name = "CONFIDENCE", nullable = false, precision = 5, scale = 2)
    private BigDecimal confidence;

    @Column(name = "SRC_IP", length = 80)
    private String srcIp;

    @Column(name = "DST_IP", length = 80)
    private String dstIp;

    @Column(name = "SRC_PORT")
    private Integer srcPort;

    @Column(name = "DST_PORT")
    private Integer dstPort;

    @Column(name = "PROTO")
    private Integer proto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "RISK_LEVEL_ID", nullable = false)
    private RiskLevelJpaEntity riskLevel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "STATUS_ID", nullable = false)
    private AlertStatusJpaEntity status;

    @Column(name = "EVENT_HASH", nullable = false, unique = true, length = 128)
    private String eventHash;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
