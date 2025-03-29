package com.xdpsx.onlineshop.constants;

public enum APIMessage {
    SUCCESS("IMESSAGE_0001"),
    CREATE_SUCCESSFUL("IMESSAGE_0002"),
    UPDATE_SUCCESSFUL("IMESSAGE_0003"),
    DELETE_SUCCESSFUL("IMESSAGE_0004");

    private final String message;

    APIMessage(String message) {
        this.message = message;
    }

    public String message() {
        return this.message;
    }
}
