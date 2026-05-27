package com.ids.domain.entity;

import com.ids.domain.exception.DomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alert {
    private Long id;
    private LocalDateTime time;
    private String reason;
    private String attack;
    private BigDecimal confidence;
    private String srcIp;
    private String dstIp;
    private Integer srcPort;
    private Integer dstPort;
    private Integer proto;
    private RiskLevel riskLevel;
    private AlertStatus status;
    private String eventHash;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Alert createNew(LocalDateTime time,
                                  String reason,
                                  String attack,
                                  BigDecimal confidence,
                                  String srcIp,
                                  String dstIp,
                                  Integer srcPort,
                                  Integer dstPort,
                                  Integer proto,
                                  RiskLevel riskLevel,
                                  AlertStatus status,
                                  String eventHash) {
        Alert alert = new Alert();
        alert.time = required(time, "Alert time is required");
        alert.confidence = required(confidence, "Confidence is required");
        alert.riskLevel = required(riskLevel, "Risk level is required");
        alert.status = required(status, "Alert status is required");
        alert.eventHash = required(eventHash, "Alert event hash is required");
        alert.reason = reason;
        alert.attack = attack;
        alert.srcIp = srcIp;
        alert.dstIp = dstIp;
        alert.srcPort = srcPort;
        alert.dstPort = dstPort;
        alert.proto = proto;
        alert.createdAt = LocalDateTime.now();
        return alert;
    }

    public void changeStatus(AlertStatus newStatus) {
        this.status = required(newStatus, "Alert status is required");
        this.updatedAt = LocalDateTime.now();
    }

    private static <T> T required(T value, String message) {
        if (value == null) {
            throw new DomainException(message);
        }
        return value;
    }
}
