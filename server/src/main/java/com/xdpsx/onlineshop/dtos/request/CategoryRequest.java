package com.xdpsx.onlineshop.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CategoryRequest {
    @NotBlank
    @Size(max = 128)
    private String name;
}
