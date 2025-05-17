package com.xdpsx.onlineshop.dtos.brand;

import java.util.List;

import com.xdpsx.onlineshop.dtos.media.ViewMediaDTO;

public record BrandDetailResponse(
        Integer id, String name, boolean publicFlg, ViewMediaDTO image, List<CategoryDTO> categories) {
    public record CategoryDTO(Integer id, String name) {}
}
