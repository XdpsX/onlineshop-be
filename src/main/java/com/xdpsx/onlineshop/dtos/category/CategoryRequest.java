package com.xdpsx.onlineshop.dtos.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {
    @NotBlank
    @Size(max = 128)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String slug;

    public void setSlug(String slug) {
        this.slug = slug.toLowerCase().replace(" ", "");
    }
}
