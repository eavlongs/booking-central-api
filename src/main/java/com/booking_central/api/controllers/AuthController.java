package com.booking_central.api.controllers;

import com.booking_central.api.dto.AuthTokens;
import com.booking_central.api.dto.TokenBody;
import com.booking_central.api.helpers.ResponseHelper;
import com.booking_central.api.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/request-token")
    public ResponseEntity<Map<String, Object>> requestToken(@RequestBody TokenBody body) {
        Optional<AuthTokens> authTokens = authService.requestToken(body);

        if (authTokens.isPresent()) {
           Map<String, Object> data = Map.of(
               "access_token", authTokens.get().getAccessToken(),
               "refresh_token", authTokens.get().getRefreshToken()
           );

           return ResponseHelper.buildSuccessResponse(data);
        } else {
            return ResponseHelper.buildBadRequestResponse("Invalid token");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestBody TokenBody body) {
        Optional<AuthTokens> authTokens = authService.refreshToken(body);

        if (authTokens.isPresent()) {
           Map<String, Object> data = Map.of(
               "access_token", authTokens.get().getAccessToken(),
               "refresh_token", authTokens.get().getRefreshToken()
           );

           return ResponseHelper.buildSuccessResponse(data);
        } else {
            return ResponseHelper.buildBadRequestResponse("Invalid token");
        }
    }

    @GetMapping("/providers")
    public ResponseEntity<Map<String, Object>> getProviders() {
        Map<String, Object> data = Map.of(
            "providers", authService.getProviders()
        );

        return ResponseHelper.buildSuccessResponse(data);
    }
}
