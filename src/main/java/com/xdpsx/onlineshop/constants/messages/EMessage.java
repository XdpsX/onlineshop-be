package com.xdpsx.onlineshop.constants.messages;

public enum EMessage implements APIMessage {
    SERVER_ERROR("E_MESSAGE_0001"),
    BAD_CREDENTIALS("E_MESSAGE_0002"),
    BAD_VALIDATION("E_MESSAGE_0003"),
    DATA_EXISTS("E_MESSAGE_0004"),
    NOT_FOUND("E_MESSAGE_0005"),
    IN_USE("E_MESSAGE_0006"),
    MODIFY_EXCLUSIVE("E_MESSAGE_0007"),
    INVALID_RESOURCE_TYPE("E_MESSAGE_0008"),
    ;

    private final String message;

    EMessage(String message) {
        this.message = message;
    }

    public String message() {
        return this.message;
    }
}
