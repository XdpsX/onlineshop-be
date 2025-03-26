package com.xdpsx.onlineshop.entities;

import java.util.List;

import jakarta.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "categories")
@EntityListeners(AuditingEntityListener.class)
public class Category extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String slug;

    @ManyToMany(mappedBy = "categories")
    private List<Brand> brands;
}
