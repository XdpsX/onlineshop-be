package com.xdpsx.ecommerce.dtos.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderRequest {
    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Order items are required")
    private List<OrderItemRequest> items;
}
