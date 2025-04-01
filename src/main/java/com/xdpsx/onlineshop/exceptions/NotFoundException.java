package com.xdpsx.onlineshop.exceptions;

import com.xdpsx.onlineshop.constants.messages.APIMessage;

public class NotFoundException extends APIException {
    public NotFoundException(String message, Object... args) {
        super(message, args);
    }

    public NotFoundException(APIMessage apiMessage, Object... args) {
        super(apiMessage.message(), args);
    }
}
