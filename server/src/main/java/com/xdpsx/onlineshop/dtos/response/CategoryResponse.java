package com.xdpsx.onlineshop.dtos.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CategoryResponse {
    private Integer id;
    private String name;
    private String slug;
}
