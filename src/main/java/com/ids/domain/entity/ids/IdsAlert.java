package com.ids.domain.entity.ids;

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
public class IdsAlert {
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
    private IdsRiskLevel idsRiskLevel;
    private IdsAlertStatus status;
    private String eventHash;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static IdsAlert createNew(LocalDateTime time,
                                  String reason,
                                  String attack,
                                  BigDecimal confidence,
                                  String srcIp,
                                  String dstIp,
                                  Integer srcPort,
                                  Integer dstPort,
                                  Integer proto,
                                  IdsRiskLevel idsRiskLevel,
                                  IdsAlertStatus status,
                                  String eventHash) {
        IdsAlert idsAlert = new IdsAlert();
        idsAlert.time = required(time, "IdsAlert time is required");
        idsAlert.confidence = required(confidence, "Confidence is required");
        idsAlert.idsRiskLevel = required(idsRiskLevel, "Risk level is required");
        idsAlert.status = required(status, "IdsAlert status is required");
        idsAlert.eventHash = required(eventHash, "IdsAlert event hash is required");
        idsAlert.reason = reason;
        idsAlert.attack = attack;
        idsAlert.srcIp = srcIp;
        idsAlert.dstIp = dstIp;
        idsAlert.srcPort = srcPort;
        idsAlert.dstPort = dstPort;
        idsAlert.proto = proto;
        idsAlert.createdAt = LocalDateTime.now();
        return idsAlert;
    }

    public void changeStatus(IdsAlertStatus newStatus) {
        this.status = required(newStatus, "IdsAlert status is required");
        this.updatedAt = LocalDateTime.now();
    }

    private static <T> T required(T value, String message) {
        if (value == null) {
            throw new DomainException(message);
        }
        return value;
    }
}
