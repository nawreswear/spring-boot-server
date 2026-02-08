package com.springjwt.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {
    private String message;
    private boolean success;
    private Object data;

    public MessageResponse(String message) {
        this.message = message;
        this.success = !message.startsWith("Error:") && !message.startsWith("Erreur:");
    }

    public MessageResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public MessageResponse(String message, boolean success, Object data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }
}
