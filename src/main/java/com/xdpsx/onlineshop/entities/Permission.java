package com.xdpsx.onlineshop.entities;

import com.xdpsx.onlineshop.entities.common.AuditEntity;
import com.xdpsx.onlineshop.entities.enums.PermissionName;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "permissions")
public class Permission extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 64, nullable = false, unique = true)
    private PermissionName name;

    private String description;
}
