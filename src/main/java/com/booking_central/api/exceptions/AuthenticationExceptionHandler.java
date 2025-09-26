package com.booking_central.api.exceptions;

import com.booking_central.api.helpers.ResponseHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

// https://www.baeldung.com/spring-security-exceptionhandler
@ControllerAdvice
public class AuthenticationExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ AuthenticationException.class })
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(Exception ex) {
        return ResponseHelper.buildErrorResponse(null, "Failed to authenticate user", HttpStatus.UNAUTHORIZED);
    }
}
