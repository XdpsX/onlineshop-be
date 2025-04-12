package com.xdpsx.onlineshop.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.xdpsx.onlineshop.dtos.brand.BrandNoCatsDTO;
import com.xdpsx.onlineshop.dtos.brand.BrandRequest;
import com.xdpsx.onlineshop.dtos.brand.BrandResponse;
import com.xdpsx.onlineshop.entities.Brand;
import com.xdpsx.onlineshop.utils.CloudinaryUploader;

@Mapper(componentModel = "spring")
public abstract class BrandMapper {
    @Autowired
    private CloudinaryUploader uploader;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    public abstract Brand fromRequestToEntity(BrandRequest request);

    abstract BrandResponse toBrandResponse(Brand entity);

    public BrandResponse fromEntityToResponse(Brand entity) {
        BrandResponse response = toBrandResponse(entity);
        return response;
    }

    public abstract BrandNoCatsDTO fromEntityToNotCatsDTO(Brand entity);
}
