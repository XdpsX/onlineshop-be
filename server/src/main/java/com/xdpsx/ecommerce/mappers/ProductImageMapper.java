package com.xdpsx.ecommerce.mappers;

import com.xdpsx.ecommerce.dtos.product.ProductImageDTO;
import com.xdpsx.ecommerce.entities.ProductImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
    ProductImageDTO fromEntityToDTO(ProductImage entity);
}
