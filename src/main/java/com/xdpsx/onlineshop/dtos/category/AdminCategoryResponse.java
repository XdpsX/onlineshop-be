package com.xdpsx.onlineshop.dtos.category;

public record AdminCategoryResponse(Integer id, String name, boolean publicFlg, String image, CategoryDTO parent) {
    public record CategoryDTO(Integer id, String name) {}
}
