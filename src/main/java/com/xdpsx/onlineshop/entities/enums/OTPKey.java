package com.xdpsx.onlineshop.entities.enums;

public enum OTPKey {
    OTP_VERIFY_EMAIL("otp:verify-email"),
    COOLDOWN_VERIFY_EMAIL("cooldown:verify-email"),
    DAILY_COUNT_VERIFY_EMAIL("daily-count:verify-email"),
    DAILY_COUNT_SEND_EMAIL("daily-count:send-email");

    private final String value;

    OTPKey(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
