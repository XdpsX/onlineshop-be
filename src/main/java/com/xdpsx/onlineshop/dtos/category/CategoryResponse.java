package com.xdpsx.onlineshop.dtos.category;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private Integer id;
    private String name;
    private List<CategoryResponse> children = new ArrayList<>();
}
