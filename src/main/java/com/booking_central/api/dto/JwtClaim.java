package com.booking_central.api.dto;

import com.booking_central.api.enums.JwtType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
public class JwtClaim {
    @NonNull
    private String id;
    @NonNull
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @NonNull
    private JwtType type;

    public JwtClaim(Claims claims) throws IllegalArgumentException, NullPointerException {
        this.setId(claims.get("id", String.class));
        this.setFirstName(claims.get("first_name", String.class));
        this.setLastName(claims.get("last_name", String.class));
        this.setType(claims.get("type", String.class)); // throws illegal argument exception if not valid
    }

    private void setType(String s) throws IllegalArgumentException {
        try {
            System.out.println(s);
            this.type = JwtType.fromString(s);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid token type");
       }
    }
}
