package com.ids.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlertStatus {
    private Long id;
    private String code;
    private String label;
    private Integer sortOrder;
}
