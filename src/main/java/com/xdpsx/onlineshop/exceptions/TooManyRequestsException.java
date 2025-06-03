package com.xdpsx.onlineshop.exceptions;

import com.xdpsx.onlineshop.constants.messages.APIMessage;

public class TooManyRequestsException extends APIException {
    public TooManyRequestsException(String message, Object... args) {
        super(message, args);
    }

    public TooManyRequestsException(APIMessage apiMessage, Object... args) {
        super(apiMessage.message(), args);
    }
}
