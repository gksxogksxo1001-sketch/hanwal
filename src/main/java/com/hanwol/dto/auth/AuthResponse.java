package com.hanwol.dto.auth;

import lombok.Data;

@Data
public class AuthResponse {
    private boolean success;
    private String message;
    private Object data;

    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public AuthResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
