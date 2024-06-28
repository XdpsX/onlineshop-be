package com.xdpsx.ecommerce.services.impl;

import com.xdpsx.ecommerce.dtos.category.CategoryRequest;
import com.xdpsx.ecommerce.dtos.category.CategoryResponse;
import com.xdpsx.ecommerce.dtos.common.PageParams;
import com.xdpsx.ecommerce.dtos.common.PageResponse;
import com.xdpsx.ecommerce.entities.Category;
import com.xdpsx.ecommerce.exceptions.BadRequestException;
import com.xdpsx.ecommerce.exceptions.ResourceNotFoundException;
import com.xdpsx.ecommerce.mappers.CategoryMapper;
import com.xdpsx.ecommerce.repositories.CategoryRepository;
import com.xdpsx.ecommerce.services.CategoryService;
import com.xdpsx.ecommerce.specifications.SimpleSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    private final SimpleSpecification<Category> spec;

    @Override
    public PageResponse<CategoryResponse> getAllCategories(PageParams request) {
        Pageable pageable = PageRequest.of(request.getPageNum() - 1, request.getPageSize());
        Page<Category> categoryPage = categoryRepository.findAll(
                spec.getSearchSpec(request.getSearch(), request.getSort()),
                pageable
        );
        List<CategoryResponse> categoryResponses = categoryPage.getContent().stream()
                .map(categoryMapper::fromEntityToResponse)
                .collect(Collectors.toList());

        return PageResponse.<CategoryResponse>builder()
                .items(categoryResponses)
                .pageNum(categoryPage.getNumber() + 1)
                .pageSize(categoryPage.getSize())
                .totalItems(categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .build();
    }

    @Override
    public CategoryResponse getCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id=[%s] not found!".formatted(id)));
        return categoryMapper.fromEntityToResponse(category);
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = categoryMapper.fromRequestToEntity(request);
        if (categoryRepository.existsByName(category.getName())){
            throw new BadRequestException("Category with name=[%s] has already existed!".formatted(category.getName()));
        }
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.fromEntityToResponse(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(Integer id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id=[%s] not found!".formatted(id)));

        if (categoryRepository.existsByName(request.getName())){
            throw new BadRequestException("Category with name=[%s] has already existed!".formatted(request.getName()));
        }

        category.setName(request.getName());
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.fromEntityToResponse(savedCategory);
    }

    @Override
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id=[%s] not found!".formatted(id)));
        categoryRepository.delete(category);
    }

}
