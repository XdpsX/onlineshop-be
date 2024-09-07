package com.xdpsx.onlineshop.mappers;

import com.xdpsx.onlineshop.dtos.product.*;
import com.xdpsx.onlineshop.entities.Product;
import com.xdpsx.onlineshop.utils.CloudinaryUploader;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {
    @Autowired
    private CloudinaryUploader uploader;

    @Mapping(target = "images", source = "request.images", ignore = true)
    public abstract Product fromCreateRequestToEntity(ProductCreateRequest request);

    @Mapping(target = "images", source = "request.images", ignore = true)
    public abstract Product fromUpdateRequestToEntity(ProductUpdateRequest request);

    @Mapping(target = "mainImage", ignore = true)
    protected abstract ProductResponse toResponse(Product entity);

    public ProductResponse fromEntityToResponse(Product entity) {
        ProductResponse response = toResponse(entity);
        response.setMainImage(uploader.getFileUrl(entity.getMainImage()));
        return response;
    }

    @Mapping(target = "mainImage", ignore = true)
    @Mapping(target = "images", ignore = true)
    protected abstract ProductDetailsDTO toDetailsDTO(Product entity);

    public ProductDetailsDTO fromEntityToDetailsDTO(Product entity) {
        ProductDetailsDTO dto = toDetailsDTO(entity);
        dto.setMainImage(uploader.getFileUrl(entity.getMainImage()));
        dto.setImages(new ArrayList<>());
        for (var image: entity.getImages()) {
            ProductImageDTO imageDTO = new ProductImageDTO(image.getId(), uploader.getFileUrl(image.getUrl()));
            dto.getImages().add(imageDTO);
        }
        return dto;
    }
}
