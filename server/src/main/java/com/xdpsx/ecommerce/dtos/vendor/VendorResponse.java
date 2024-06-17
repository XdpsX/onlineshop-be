package com.xdpsx.ecommerce.dtos.vendor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VendorResponse {
    private Integer id;
    private String name;
    private String logo;
}
