package com.xdpsx.onlineshop.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.xdpsx.onlineshop.dtos.category.AdminCategoryResponse;
import com.xdpsx.onlineshop.dtos.category.CategoryResponse;
import com.xdpsx.onlineshop.dtos.category.CategoryTreeResponse;
import com.xdpsx.onlineshop.dtos.category.CreateCategoryRequest;
import com.xdpsx.onlineshop.entities.Category;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "brands", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toEntity(CreateCategoryRequest request);

    @Mapping(target = "image", source = "entity.image.url")
    @Mapping(target = "parent", source = "entity.parent")
    AdminCategoryResponse toAdminCategoryResponse(Category entity);

    CategoryResponse toResponse(Category entity);

    @Mapping(target = "children", ignore = true)
    CategoryTreeResponse toCategoryTreeResponse(Category entity);
}
