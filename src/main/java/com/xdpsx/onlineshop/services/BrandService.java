package com.xdpsx.onlineshop.services;

import com.xdpsx.onlineshop.dtos.brand.*;
import com.xdpsx.onlineshop.dtos.common.CheckExistResponse;
import com.xdpsx.onlineshop.dtos.common.ModifyExclusiveDTO;
import com.xdpsx.onlineshop.dtos.common.PageResponse;

public interface BrandService {
    PageResponse<AdminBrandResponse> getAdminBrands(AdminBrandFilter filter);

    BrandDetailResponse getAdminBrandDetail(Integer id);

    BrandDetailResponse createBrand(CreateBrandRequest request);

    BrandDetailResponse updateBrand(Integer id, UpdateBrandRequest request);

    void deleteBrand(Integer id, ModifyExclusiveDTO request);

    CheckExistResponse checkBrandExist(BrandExistRequest request);
}
