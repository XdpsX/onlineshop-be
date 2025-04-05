package com.xdpsx.onlineshop.exceptions;

import com.xdpsx.onlineshop.constants.messages.APIMessage;

public class InvalidResourceTypeException extends APIException {
    public InvalidResourceTypeException(String message, Object... args) {
        super(message, args);
    }

    public InvalidResourceTypeException(APIMessage apiMessage, Object... args) {
        super(apiMessage.message(), args);
    }
}
