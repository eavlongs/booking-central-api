package com.booking_central.api.services;

import com.booking_central.api.enums.JwtType;
import com.booking_central.api.models.User;
import com.booking_central.api.security.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {
    @Autowired
    private JwtService jwtService;

    public String generateAccessToken(User user) {
        return jwtService.buildToken(user, JwtType.ACCESS_TOKEN, Map.of("test", "lol"));
    }
    
    public Claims extractClaims(String token) {
        Claims claims = jwtService.extractClaims(token);

        return jwtService.extractClaims(token);
    }
}
