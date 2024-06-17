package com.xdpsx.ecommerce.mappers;

import com.xdpsx.ecommerce.dtos.vendor.VendorRequest;
import com.xdpsx.ecommerce.dtos.vendor.VendorResponse;
import com.xdpsx.ecommerce.entities.Vendor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VendorMapper {
    Vendor fromRequestToEntity(VendorRequest request);
    VendorResponse fromEntityToResponse(Vendor entity);
}
