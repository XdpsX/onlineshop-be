package com.xdpsx.onlineshop.utils;

import jakarta.servlet.http.HttpServletRequest;

public class HttpHelper {
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0]; // In case of multiple proxies, take the first IP
        }
        return request.getRemoteAddr();
    }
}
