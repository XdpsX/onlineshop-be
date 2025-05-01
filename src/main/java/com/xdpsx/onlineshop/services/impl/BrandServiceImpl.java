package com.xdpsx.onlineshop.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.xdpsx.onlineshop.constants.messages.EMessage;
import com.xdpsx.onlineshop.dtos.brand.*;
import com.xdpsx.onlineshop.dtos.common.PageResponse;
import com.xdpsx.onlineshop.entities.Brand;
import com.xdpsx.onlineshop.entities.Category;
import com.xdpsx.onlineshop.entities.Media;
import com.xdpsx.onlineshop.entities.enums.MediaResourceType;
import com.xdpsx.onlineshop.exceptions.DuplicateException;
import com.xdpsx.onlineshop.exceptions.InvalidResourceTypeException;
import com.xdpsx.onlineshop.exceptions.NotFoundException;
import com.xdpsx.onlineshop.mappers.BrandMapper;
import com.xdpsx.onlineshop.mappers.PageMapper;
import com.xdpsx.onlineshop.repositories.BrandRepository;
import com.xdpsx.onlineshop.repositories.CategoryRepository;
import com.xdpsx.onlineshop.repositories.MediaRepository;
import com.xdpsx.onlineshop.repositories.specs.BrandSpecification;
import com.xdpsx.onlineshop.services.BrandService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandMapper brandMapper;
    private final MediaRepository mediaRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public PageResponse<AdminBrandResponse> getAdminBrands(AdminBrandFilter filter) {
        Specification<Brand> spec = BrandSpecification.getInstance()
                .buildAdminBrandsSpec(filter.getName(), filter.getPublicFlg(), filter.getSort());
        Page<Brand> brandPage =
                brandRepository.findAll(spec, PageRequest.of(filter.getPageNum() - 1, filter.getPageSize()));
        return PageMapper.toPageResponse(brandPage, BrandMapper.INSTANCE::toAdminBrandResponse);
    }

    @Transactional
    @Override
    public AdminBrandResponse createBrand(CreateBrandRequest request) {
        Brand brand = BrandMapper.INSTANCE.toEntity(request);
        if (brandRepository.existsByName(request.name())) {
            throw new DuplicateException(EMessage.DATA_EXISTS, request.name());
        }

        if (request.imageId() != null) {
            Media image = mediaRepository
                    .findById(request.imageId())
                    .orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, request.imageId()));
            if (!image.getResourceType().equals(MediaResourceType.BRAND)) {
                throw new InvalidResourceTypeException(EMessage.INVALID_RESOURCE_TYPE);
            }
            image.setTempFlg(false);
            brand.setImage(image);
        }
        if (request.categoryIds() != null) {
            List<Category> categories = fetchCategories(request.categoryIds());
            brand.setCategories(categories);
        }

        Brand savedBrand = brandRepository.save(brand);
        return BrandMapper.INSTANCE.toAdminBrandResponse(savedBrand);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public AdminBrandResponse updateBrand(Integer id, BrandRequest request) {
        //        Brand existingBrand = brandRepository
        //                .findById(id)
        //                .orElseThrow(() -> new NotFoundException("Brand with id=%s not found".formatted(id)));
        //
        //        // Update name
        //        if (!existingBrand.getName().equals(request.getName())) {
        //            if (brandRepository.existsByName(request.getName())) {
        //                throw new DuplicateException("Brand with name=%s already
        // exists".formatted(request.getName()));
        //            }
        //            existingBrand.setName(request.getName());
        //        }
        //
        //        // Update categories
        //        if (request.getCategoryIds() != null) {
        //            List<Category> newCategories = fetchCategories(request.getCategoryIds());
        //            existingBrand.setCategories(newCategories);
        //        }
        //
        //        // Update logo
        //        if (request.getLogo() != null) {
        //            CloudinaryUploadResponse response = uploader.uploadFile(request.getLogo(), uploadOptions);
        //        }
        //
        //        Brand updatedBrand = brandRepository.save(existingBrand);
        //        return brandMapper.fromEntityToResponse(updatedBrand);
        return null;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void deleteBrand(Integer id) {
        Brand existingBrand = brandRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Brand with id=%s not found".formatted(id)));
        //        long countBrands = brandRepository.countBrandsInOtherTables(id);
        //        if (countBrands > 0){
        //            throw new BadRequestException(i18nUtils.getBrandCannotDeleteMsg(existingBrand.getName()));
        //        }
        brandRepository.delete(existingBrand);
    }

    @Override
    public Map<String, Boolean> checkExistsBrand(String name) {
        Map<String, Boolean> exists = new HashMap<>();
        exists.put("nameExists", brandRepository.existsByName(name));
        return exists;
    }

    //    @Transactional(readOnly = true)
    @Override
    public List<BrandNoCatsDTO> listBrandsByCategoryId(Integer categoryId) {
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with id=%s not found".formatted(categoryId)));
        //        List<Brand> brands = category.getBrands();
        List<Brand> brands = brandRepository.findBrandsByCategoryId(category.getId());
        return brands.stream().map(brandMapper::fromEntityToNotCatsDTO).collect(Collectors.toList());
    }

    private List<Category> fetchCategories(Set<Integer> categoryIds) {
        return categoryIds.stream()
                .map(categoryId -> categoryRepository
                        .findPublicById(categoryId)
                        .orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, categoryId)))
                .collect(Collectors.toList());
    }
}
