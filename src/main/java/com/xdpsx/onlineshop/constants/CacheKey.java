package com.xdpsx.onlineshop.constants;

import com.xdpsx.onlineshop.entities.enums.OTPKey;

public class CacheKey {
    public static String buildOtpKey(OTPKey otpKey, String email) {
        return otpKey.value() + ":" + email;
    }

    public static String buildLogoutKey(String jti) {
        return "blacklist:" + jti;
    }
}
