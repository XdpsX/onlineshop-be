package com.xdpsx.onlineshop.constants;

public class SecurityConstants {
    public static final String[] PUBLIC_ENDPOINTS = {
            "/auth/**"
    };
    public static final String[] PUBLIC_GET_ENDPOINTS = {
            "/categories/get-all",
            "/categories/*/brands",
            "/products/*",
            "/products"
    };

    public static final String ROLE_PREFIX = "ROLE_";
}
