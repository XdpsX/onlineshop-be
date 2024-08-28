package com.xdpsx.onlineshop.mappers;

import com.xdpsx.onlineshop.dtos.request.CategoryRequest;
import com.xdpsx.onlineshop.dtos.response.CategoryResponse;
import com.xdpsx.onlineshop.entities.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category fromRequestToEntity(CategoryRequest request);
    CategoryResponse fromEntityToResponse(Category entity);
}
