package com.booking_central.api.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum JwtType {
    @JsonProperty("access_token")
    ACCESS_TOKEN("access_token"),
    @JsonProperty("refresh_token")
    REFRESH_TOKEN("refresh_token"),
    @JsonProperty("auth_request")
    AUTH_REQUEST("auth_request");

    private final String text;

    JwtType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static JwtType fromString(String s) {
        for (JwtType type : JwtType.values()) {
            if (type.text.equalsIgnoreCase(s)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid token type");
    }
}
