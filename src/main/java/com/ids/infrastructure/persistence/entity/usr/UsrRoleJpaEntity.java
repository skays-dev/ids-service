package com.ids.infrastructure.persistence.entity.usr;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USR_ROLE", catalog = "u835525338_IDS_users")
@Getter
@Setter
@NoArgsConstructor
public class UsrRoleJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true, length = 80)
    private String name;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;
}
