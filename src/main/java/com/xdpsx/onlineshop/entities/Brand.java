package com.xdpsx.onlineshop.entities;

import java.util.List;

import jakarta.persistence.*;

import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "brands")
public class Brand extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 64, nullable = false, unique = true)
    private String name;

    private boolean publicFlg;

    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Media image;

    @ManyToMany
    @JoinTable(
            name = "category_brands",
            joinColumns = @JoinColumn(name = "brand_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private List<Category> categories;
}
