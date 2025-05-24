package com.xdpsx.onlineshop.entities;

import com.xdpsx.onlineshop.entities.common.AuditEntity;
import jakarta.persistence.*;

import com.xdpsx.onlineshop.entities.enums.AuthProvider;

import lombok.*;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    private String name;

    @Column(length = 128, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne
    private Media avatar;

    @Column(length = 15)
    private String phoneNumber;

    private boolean enabled;

    private boolean locked;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AuthProvider authProvider = AuthProvider.SYSTEM;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

}
