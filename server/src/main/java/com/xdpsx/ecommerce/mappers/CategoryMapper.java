package com.xdpsx.ecommerce.mappers;

import com.xdpsx.ecommerce.dtos.category.CategoryRequest;
import com.xdpsx.ecommerce.dtos.category.CategoryResponse;
import com.xdpsx.ecommerce.entities.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category fromRequestToEntity(CategoryRequest request);
    CategoryResponse fromEntityToResponse(Category entity);
}
