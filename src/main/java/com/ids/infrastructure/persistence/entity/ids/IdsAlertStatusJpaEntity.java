package com.ids.infrastructure.persistence.entity.ids;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "IDS_ALERT_STATUS", catalog = "u835525338_IDS_main")
@Getter
@Setter
@NoArgsConstructor
public class IdsAlertStatusJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODE", nullable = false, unique = true, length = 30)
    private String code;

    @Column(name = "LABEL", nullable = false, length = 80)
    private String label;

    @Column(name = "SORT_ORDER", nullable = false)
    private Integer sortOrder;
}
