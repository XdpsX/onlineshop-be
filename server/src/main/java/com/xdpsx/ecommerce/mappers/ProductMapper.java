package com.xdpsx.ecommerce.mappers;

import com.xdpsx.ecommerce.dtos.product.ProductRequest;
import com.xdpsx.ecommerce.dtos.product.ProductResponse;
import com.xdpsx.ecommerce.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product fromRequestToEntity(ProductRequest request);
    ProductResponse fromEntityToResponse(Product entity);
}
