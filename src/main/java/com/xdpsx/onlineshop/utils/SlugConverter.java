package com.xdpsx.onlineshop.utils;

import java.text.Normalizer;

public class SlugConverter {
    public static String toSlug(String str) {
        // Chuyển đổi về chữ thường
        String slug = str.toLowerCase();
        slug = slug.replace("đ", "d").replace("Đ", "D");

        // Xóa dấu và chuyển đổi ký tự đặc biệt
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = slug.replaceAll("[^\\p{ASCII}]", "");

        // Thay thế khoảng trắng và ký tự đặc biệt bằng dấu gạch ngang
        slug = slug.replaceAll("[\\s+]", "-"); // Thay thế khoảng trắng
        slug = slug.replaceAll("[^a-z0-9-]", ""); // Loại bỏ ký tự không hợp lệ

        // Xóa các dấu gạch ngang liên tiếp
        slug = slug.replaceAll("-+", "-");

        // Xóa dấu gạch ngang ở đầu và cuối
        slug = slug.replaceAll("^-|-$", "");

        return slug;
    }
}
