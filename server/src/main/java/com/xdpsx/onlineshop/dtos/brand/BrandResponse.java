package com.xdpsx.onlineshop.dtos.brand;

import com.xdpsx.onlineshop.dtos.category.CategoryResponse;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class BrandResponse {
    private Integer id;
    private String name;
    private String logo;
    private List<CategoryResponse> categories;
}
