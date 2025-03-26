package com.xdpsx.onlineshop.entities;

import com.xdpsx.onlineshop.entities.ids.CartItemId;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "cart_items")
@EntityListeners(AuditingEntityListener.class)
public class CartItem extends AuditEntity{
    @EmbeddedId
    private CartItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;
}
