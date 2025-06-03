package com.xdpsx.onlineshop.entities.enums;

import lombok.Getter;

@Getter
public enum EmailTemplate {
    VERIFY_EMAIL("Verify your email", "verify_email");

    private final String subject;
    private final String templateName;

    EmailTemplate(String subject, String templateName) {
        this.subject = subject;
        this.templateName = templateName;
    }
}
