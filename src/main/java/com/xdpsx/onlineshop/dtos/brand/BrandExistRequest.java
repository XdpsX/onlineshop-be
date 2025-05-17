package com.xdpsx.onlineshop.dtos.brand;

import jakarta.validation.constraints.NotBlank;

public record BrandExistRequest(@NotBlank String name) {}
