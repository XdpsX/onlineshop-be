package com.xdpsx.onlineshop.dtos.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record UpdateCategoryDTO(
        @NotBlank
        @Size(max = 128)
        String name,

        boolean publicFlg,

        String imageId,

        Integer parentId,

        @NotNull
        LocalDateTime lastRetrievedAt
) {
}