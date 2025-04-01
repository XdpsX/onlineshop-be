package com.xdpsx.onlineshop.constants.messages;

public enum EMessage implements APIMessage {
    SERVER_ERROR("E_MESSAGE_0001"),
    BAD_CREDENTIALS("E_MESSAGE_0002"),
    BAD_VALIDATION("E_MESSAGE_0003");

    private final String message;

    EMessage(String message) {
        this.message = message;
    }

    public String message() {
        return this.message;
    }
}
