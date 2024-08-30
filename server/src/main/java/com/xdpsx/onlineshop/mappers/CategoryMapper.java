package com.xdpsx.onlineshop.mappers;

import com.xdpsx.onlineshop.dtos.category.CategoryCreateRequest;
import com.xdpsx.onlineshop.dtos.category.CategoryResponse;
import com.xdpsx.onlineshop.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "brands", ignore = true)
    Category fromRequestToEntity(CategoryCreateRequest request);
    CategoryResponse fromEntityToResponse(Category entity);
}
