package com.xdpsx.onlineshop.dtos.brand;

import java.util.List;

public record AdminBrandResponse(
        Integer id, String name, boolean publicFlg, String image, List<CategoryDTO> categories) {
    public record CategoryDTO(Integer id, String name) {}
}
