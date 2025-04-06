package com.xdpsx.onlineshop.services.impl;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.xdpsx.onlineshop.repositories.exp.CategoryExpSpecificationBuilder;
import com.xdpsx.onlineshop.repositories.exp.SearchExpRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xdpsx.onlineshop.constants.messages.EMessage;
import com.xdpsx.onlineshop.dtos.category.CategoryResponse;
import com.xdpsx.onlineshop.dtos.category.CreateCategoryDTO;
import com.xdpsx.onlineshop.dtos.category.UpdateCategoryDTO;
import com.xdpsx.onlineshop.dtos.common.ModifyExclusiveDTO;
import com.xdpsx.onlineshop.dtos.common.PageParams;
import com.xdpsx.onlineshop.dtos.common.PageResponse;
import com.xdpsx.onlineshop.entities.Category;
import com.xdpsx.onlineshop.entities.Media;
import com.xdpsx.onlineshop.entities.enums.MediaResourceType;
import com.xdpsx.onlineshop.exceptions.*;
import com.xdpsx.onlineshop.mappers.CategoryMapper;
import com.xdpsx.onlineshop.mappers.PageMapper;
import com.xdpsx.onlineshop.repositories.CategoryRepository;
import com.xdpsx.onlineshop.repositories.MediaRepository;
import com.xdpsx.onlineshop.repositories.specs.BasicSpecification;
import com.xdpsx.onlineshop.services.CategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final PageMapper pageMapper;
    private final CategoryRepository categoryRepository;
    private final MediaRepository mediaRepository;

    private final BasicSpecification<Category> spec;
    private final SearchExpRepository searchExpRepository;

    @Override
    public PageResponse<CategoryResponse> getCategoriesPage(PageParams params) {
        Page<Category> categoryPage = categoryRepository.findAll(
                spec.getFiltersSpec(params.getSearch(), params.getSort()),
                PageRequest.of(params.getPageNum() - 1, params.getPageSize()));
        return pageMapper.toCategoryPageResponse(categoryPage);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll(spec.getSortSpec("name")).stream()
                .map(CategoryMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CreateCategoryDTO request) {
        Category category = CategoryMapper.INSTANCE.toEntity(request);

        if (categoryRepository.existsByName(request.name())) {
            throw new DuplicateException(EMessage.DATA_EXISTS, request.name());
        }

        if (request.parentId() != null) {
            Category parent = categoryRepository
                    .findById(request.parentId())
                    .orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, request.parentId()));
            category.setParent(parent);
        }

        if (request.imageId() != null) {
            Media image = mediaRepository
                    .findById(request.imageId())
                    .orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, request.imageId()));
            if (!image.getResourceType().equals(MediaResourceType.CATEGORY)) {
                throw new InvalidResourceTypeException(EMessage.INVALID_RESOURCE_TYPE);
            }
            image.setTempFlg(false);
            category.setImage(image);
        }

        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.INSTANCE.toResponse(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Integer id, UpdateCategoryDTO request) {
        Category category = categoryRepository
                .findByIdWithParent(id)
                .orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, id));

        if (category.getUpdatedAt() != null && !request.lastRetrievedAt().isAfter(category.getUpdatedAt())) {
            throw new ModifyExclusiveException(EMessage.MODIFY_EXCLUSIVE);
        }

        // Update name
        if (!category.getName().equals(request.name())) {
            if (categoryRepository.existsByName(request.name())) {
                throw new DuplicateException(EMessage.DATA_EXISTS, request.name());
            }
            category.setName(request.name());
        }

        category.setPublicFlg(request.publicFlg());
        // Update image
        updateCategoryImage(category, request.imageId());

        // Update parent
        if (!isSameParent(category, request.parentId())) {
            category.setParent(getParentCategory(request.parentId()));
        }

        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.INSTANCE.toResponse(savedCategory);
    }

    private void updateCategoryImage(Category category, String newImageId) {
        Media oldImage = category.getImage();

        if (oldImage == null && newImageId == null) return;

        // If have old image:
        // 1. No new image (newImageId == null) => Delete old image
        // 2. New image != old image => Delete old image
        if (oldImage != null && !oldImage.getId().equals(newImageId)) {
            oldImage.setDeleteFlg(true);
            mediaRepository.save(oldImage);
            category.setImage(null);
        }

        // if have new image and new image != old image => Update image
        if (newImageId != null && (oldImage == null || !oldImage.getId().equals(newImageId))) {
            Media newImage = mediaRepository
                    .findById(newImageId)
                    .orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, newImageId));
            if (!newImage.getResourceType().equals(MediaResourceType.CATEGORY)) {
                throw new InvalidResourceTypeException(EMessage.INVALID_RESOURCE_TYPE);
            }

            newImage.setTempFlg(false);
            mediaRepository.save(newImage);

            category.setImage(newImage);
        }
    }

    private boolean isSameParent(Category category, Integer newParentId) {
        return (category.getParent() == null && newParentId == null)
                || (category.getParent() != null && category.getParent().getId().equals(newParentId));
    }

    private Category getParentCategory(Integer parentId) {
        return (parentId == null)
                ? null
                : categoryRepository
                        .findById(parentId)
                        .orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, parentId));
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id, ModifyExclusiveDTO request) {
        Category category =
                categoryRepository.findById(id).orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, id));
        if (!request.lastRetrievedAt().isAfter(category.getUpdatedAt())) {
            throw new ModifyExclusiveException(EMessage.MODIFY_EXCLUSIVE);
        }
        long countCategories = categoryRepository.countCategoriesInOtherTables(id);
        if (countCategories > 0) {
            throw new InUseException(EMessage.IN_USE);
        }
        if (category.getImage() != null) {
            Media image = category.getImage();
            image.setDeleteFlg(true);
            mediaRepository.save(image);
        }
        categoryRepository.delete(category);
    }

    @Override
    public Map<String, Boolean> checkExistsCat(String name, String slug) {
        Map<String, Boolean> exists = new HashMap<>();
        exists.put("nameExists", categoryRepository.existsByName(name));
        return exists;
    }

    // NOTE: Experimental 1
    // page=1&size=10&sort=name:asc|desc
    public void getAllCategoriesWithSortExp(int page, int size, String sort) {
        List<Sort.Order> sorts = new ArrayList<>();

        if (StringUtils.hasText(sort)) {
            // name:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                if (matcher.group(3).equals("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sorts));
        Page<Category> categories = categoryRepository.findAll(pageable);
//        categories.stream().map()
    }

    // NOTE: Experimental 2
    public void getAllCategoriesWithMultiSortsExp(int page, int size, String... sorts) {
        List<Sort.Order> orderSorts = new ArrayList<>();

        for (String sort: sorts) {
            // name:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                if (matcher.group(3).equals("asc")) {
                    orderSorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    orderSorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }


        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(orderSorts));
        Page<Category> categories = categoryRepository.findAll(pageable);
    }

    public void advanceSearchWithSpecifications(Pageable pageable, String[] user, String[] address) {
        if (user != null && address != null) {
//            return searchExpRepository.searchCategoryByCriteriaWithJoin(pageable, user, address);
        } else if (user != null) {
            CategoryExpSpecificationBuilder builder = new CategoryExpSpecificationBuilder();
            String SEARCH_SPEC_OPERATOR = "(\\w+?)([<:>~!])(.*)(\\p{Punct}?)(\\p{Punct}?)";
            Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
            for (String s : user) {
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    builder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
                }
            }

            Page<Category> categories = categoryRepository.findAll(Objects.requireNonNull(builder.build()), pageable);

//            return convertToPageResponse(categories, pageable);
        }

//        return convertToPageResponse(categoryRepository.findAll(pageable), pageable);
    }

}
