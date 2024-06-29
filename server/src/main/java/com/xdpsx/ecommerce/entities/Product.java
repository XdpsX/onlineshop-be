package com.xdpsx.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
public class Product extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    private double discountPercent;

    private boolean inStock;

    private boolean enabled;

    @Column(length = 4096)
    private String description;

    @Column(nullable = false)
    private String mainImage;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<ProductImage> images;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "vendor_id", referencedColumnName = "id")
    private Vendor vendor;

    public Product(Long id, String name, double price, double discountPercent, boolean inStock, boolean enabled,
                   String mainImage, Category category, Vendor vendor) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discountPercent = discountPercent;
        this.inStock = inStock;
        this.enabled = enabled;
        this.mainImage = mainImage;
        this.category = category;
        this.vendor = vendor;
    }
}
