package com.ids.domain.entity.usr;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsrRole {
    private Long id;
    private String name;
    private String description;
}
