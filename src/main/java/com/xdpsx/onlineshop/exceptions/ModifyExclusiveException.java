package com.xdpsx.onlineshop.exceptions;

import com.xdpsx.onlineshop.constants.messages.APIMessage;

public class ModifyExclusiveException extends APIException {
    public ModifyExclusiveException(String message, Object... args) {
        super(message, args);
    }

    public ModifyExclusiveException(APIMessage apiMessage, Object... args) {
        super(apiMessage.message(), args);
    }
}
