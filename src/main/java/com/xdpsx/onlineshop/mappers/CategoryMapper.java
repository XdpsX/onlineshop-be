package com.xdpsx.onlineshop.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.xdpsx.onlineshop.dtos.category.CategoryRequest;
import com.xdpsx.onlineshop.dtos.category.CategoryResponse;
import com.xdpsx.onlineshop.entities.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "brands", ignore = true)
    Category fromRequestToEntity(CategoryRequest request);

    CategoryResponse fromEntityToResponse(Category entity);
}
