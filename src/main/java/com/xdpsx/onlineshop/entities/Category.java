package com.xdpsx.onlineshop.entities;

import java.util.List;

import jakarta.persistence.*;

import lombok.experimental.SuperBuilder;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "categories")
public class Category extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    private boolean publicFlg;

    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Media image;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    //    @OrderBy("name asc")
    private List<Category> children;

    @ManyToMany(mappedBy = "categories")
    private List<Brand> brands;

}
