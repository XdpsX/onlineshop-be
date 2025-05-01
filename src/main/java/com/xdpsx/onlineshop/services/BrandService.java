package com.xdpsx.onlineshop.services;

import java.util.List;
import java.util.Map;

import com.xdpsx.onlineshop.dtos.brand.*;
import com.xdpsx.onlineshop.dtos.common.PageResponse;

public interface BrandService {
    PageResponse<AdminBrandResponse> getAdminBrands(AdminBrandFilter filter);

    AdminBrandResponse createBrand(CreateBrandRequest request);

    AdminBrandResponse updateBrand(Integer id, BrandRequest request);

    void deleteBrand(Integer id);

    Map<String, Boolean> checkExistsBrand(String name);

    List<BrandNoCatsDTO> listBrandsByCategoryId(Integer categoryId);
}
