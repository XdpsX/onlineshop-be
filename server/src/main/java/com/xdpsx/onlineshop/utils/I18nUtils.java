package com.xdpsx.onlineshop.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class I18nUtils {
    private final MessageSource messageSource;

    public String getCatCannotDeleteMsg(String name){
        return getMessage("category.can-not-delete", name);
    }

    private String getMessage(String msgKey, Object... params) {
        return messageSource.getMessage(msgKey, params, LocaleContextHolder.getLocale());
    }
}
