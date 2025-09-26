package com.booking_central.api.controllers;

import com.booking_central.api.dto.JwtClaim;
import com.booking_central.api.models.User;
import com.booking_central.api.services.AuthService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
public class AuthController {
    @Autowired
    private AuthService authService;
    
    @GetMapping("/test")
    public String test() {
        User user = new User(UUID.randomUUID().toString(), "Eav Long", "Sok", null, null);
        return authService.generateAccessToken(user);
    }

    @GetMapping("/claims")
    public Claims extractClaimsTest(@RequestParam String token) {
        return authService.extractClaims(token);
    }

    @GetMapping("/test-auth")
    public JwtClaim testAuth(Authentication authentication) {
        JwtClaim jwtClaim = (JwtClaim) authentication.getPrincipal();
        return  jwtClaim;
    }
}
