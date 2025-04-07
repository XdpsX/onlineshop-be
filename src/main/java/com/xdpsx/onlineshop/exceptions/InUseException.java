package com.xdpsx.onlineshop.exceptions;

import com.xdpsx.onlineshop.constants.messages.APIMessage;

public class InUseException extends APIException {
    public InUseException(String message, Object... args) {
        super(message, args);
    }

    public InUseException(APIMessage apiMessage, Object... args) {
        super(apiMessage.message(), args);
    }
}
