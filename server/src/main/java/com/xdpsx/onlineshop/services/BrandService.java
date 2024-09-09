package com.xdpsx.onlineshop.services;

import com.xdpsx.onlineshop.dtos.brand.BrandNoCatsDTO;
import com.xdpsx.onlineshop.dtos.brand.BrandRequest;
import com.xdpsx.onlineshop.dtos.brand.BrandResponse;
import com.xdpsx.onlineshop.dtos.common.PageParams;
import com.xdpsx.onlineshop.dtos.common.PageResponse;

import java.util.List;
import java.util.Map;

public interface BrandService {
    PageResponse<BrandResponse> listBrandsByPage(PageParams params);
    BrandResponse createBrand(BrandRequest request);
    BrandResponse updateBrand(Integer id, BrandRequest request);
    void deleteBrand(Integer id);

    Map<String, Boolean> checkExistsBrand(String name);
    List<BrandNoCatsDTO> listBrandsByCategoryId(Integer categoryId);
}
