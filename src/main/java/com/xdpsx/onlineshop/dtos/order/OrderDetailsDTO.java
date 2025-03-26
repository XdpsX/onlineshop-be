package com.xdpsx.onlineshop.dtos.order;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data @SuperBuilder
public class OrderDetailsDTO extends OrderDTO{
    private String username;
    private List<OrderItemResponse> items;
}
