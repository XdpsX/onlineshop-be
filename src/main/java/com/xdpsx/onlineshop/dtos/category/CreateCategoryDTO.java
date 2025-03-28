package com.xdpsx.onlineshop.dtos.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategoryDTO {
    @NotBlank
    @Size(max = 128)
    private String name;

    @Size(max = 255)
    private String image;

    private boolean publicFlg;

    private Integer parentId;
}
