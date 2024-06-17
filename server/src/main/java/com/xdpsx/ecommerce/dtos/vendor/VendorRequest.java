package com.xdpsx.ecommerce.dtos.vendor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VendorRequest {
    @NotBlank(message = "Vendor name is required")
    @Size(max = 128, message = "Vendor name cannot exceed 128 characters")
    private String name;
}
