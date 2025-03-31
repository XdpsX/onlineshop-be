package com.xdpsx.onlineshop.exceptions;

import com.xdpsx.onlineshop.constants.messages.APIMessage;

public class BadRequestException extends APIException {
    public BadRequestException(String message, Object... args) {
        super(message, args);
    }

    public BadRequestException(APIMessage apiMessage, Object... args) {
        super(apiMessage.message(), args);
    }
}
