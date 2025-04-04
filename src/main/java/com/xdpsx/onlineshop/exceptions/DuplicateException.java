package com.xdpsx.onlineshop.exceptions;

import com.xdpsx.onlineshop.constants.messages.APIMessage;

public class DuplicateException extends APIException {
    public DuplicateException(String message, Object... args) {
        super(message, args);
    }

    public DuplicateException(APIMessage apiMessage, Object... args) {
        super(apiMessage.message(), args);
    }
}
