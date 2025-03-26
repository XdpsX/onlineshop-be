package com.xdpsx.onlineshop.dtos.brand;

import java.util.List;

import com.xdpsx.onlineshop.dtos.category.CategoryResponse;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandResponse {
    private Integer id;
    private String name;
    private String logo;
    private List<CategoryResponse> categories;
}
