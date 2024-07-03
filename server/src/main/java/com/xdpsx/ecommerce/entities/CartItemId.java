package com.xdpsx.ecommerce.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class CartItemId implements Serializable {
    private Long userId;
    private Long productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartItemId that = (CartItemId) o;

        if (!userId.equals(that.userId)) return false;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        return result;
    }
}
