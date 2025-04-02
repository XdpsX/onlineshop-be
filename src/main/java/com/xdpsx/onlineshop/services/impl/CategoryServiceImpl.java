package com.xdpsx.onlineshop.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.xdpsx.onlineshop.constants.messages.EMessage;
import com.xdpsx.onlineshop.entities.Media;
import com.xdpsx.onlineshop.repositories.MediaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.xdpsx.onlineshop.dtos.category.CategoryResponse;
import com.xdpsx.onlineshop.dtos.category.CreateCategoryDTO;
import com.xdpsx.onlineshop.dtos.common.PageParams;
import com.xdpsx.onlineshop.dtos.common.PageResponse;
import com.xdpsx.onlineshop.entities.Category;
import com.xdpsx.onlineshop.exceptions.DuplicateException;
import com.xdpsx.onlineshop.exceptions.NotFoundException;
import com.xdpsx.onlineshop.mappers.CategoryMapper;
import com.xdpsx.onlineshop.mappers.PageMapper;
import com.xdpsx.onlineshop.repositories.CategoryRepository;
import com.xdpsx.onlineshop.repositories.specs.BasicSpecification;
import com.xdpsx.onlineshop.services.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final PageMapper pageMapper;
    private final CategoryRepository categoryRepository;
    private final MediaRepository mediaRepository;

    private final BasicSpecification<Category> spec;

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll(spec.getSortSpec("name")).stream()
                .map(CategoryMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse createCategory(CreateCategoryDTO request) {
        Category category = CategoryMapper.INSTANCE.toEntity(request);

        if (categoryRepository.existsByName(request.name())) {
            throw new DuplicateException(EMessage.DATA_EXISTS, request.name());
        }

        if (request.parentId() != null) {
            Category parent = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, request.parentId()));
            category.setParent(parent);
        }

        if (request.imageId() != null) {
            Media image = mediaRepository.findById(request.imageId())
                    .orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, request.imageId()));
            image.setTempFlg(false);
            category.setImage(image);
        }

        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.INSTANCE.toResponse(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(Integer id, CreateCategoryDTO request) {
        Category existingCat = categoryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id=%s not found".formatted(id)));

        // Update name
        if (!existingCat.getName().equals(request.name())) {
            if (categoryRepository.existsByName(request.name())) {
                throw new DuplicateException(EMessage.DATA_EXISTS, request.name());
            }
            existingCat.setName(request.name());
        }

        Category savedCategory = categoryRepository.save(existingCat);
        return CategoryMapper.INSTANCE.toResponse(savedCategory);
    }

    @Override
    public void deleteCategory(Integer id) {
        Category existingCat = categoryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id=%s not found".formatted(id)));
        long countCategories = categoryRepository.countCategoriesInOtherTables(id);
        if (countCategories > 0) {
            //            throw new BadRequestException(i18nUtils.getCatCannotDeleteMsg(existingCat.getName()));
        }
        categoryRepository.delete(existingCat);
    }

    @Override
    public PageResponse<CategoryResponse> getCategoriesPage(PageParams params) {
        Page<Category> categoryPage = categoryRepository.findAll(
                spec.getFiltersSpec(params.getSearch(), params.getSort()),
                PageRequest.of(params.getPageNum() - 1, params.getPageSize()));
        return pageMapper.toCategoryPageResponse(categoryPage);
    }

    @Override
    public Map<String, Boolean> checkExistsCat(String name, String slug) {
        Map<String, Boolean> exists = new HashMap<>();
        exists.put("nameExists", categoryRepository.existsByName(name));
        return exists;
    }
}
