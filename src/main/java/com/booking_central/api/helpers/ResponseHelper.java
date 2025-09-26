package com.booking_central.api.helpers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHelper {
    public static ResponseEntity<Map<String, Object>> buildSuccessResponse(Map<String, Object> data, String message) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", message);
        responseBody.put("data", data);
        responseBody.put("success", true);

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<Map<String, Object>> buildSuccessResponse(Map<String, Object> data) {
        return buildSuccessResponse(data, DefaultResponseMessages.REQUEST_SUCCESSFUL);
    }

    public static ResponseEntity<Map<String, Object>> buildSuccessResponse() {
        return buildSuccessResponse(null, DefaultResponseMessages.REQUEST_SUCCESSFUL);
    }

    public static ResponseEntity<Map<String, Object>> buildErrorResponse(Map<String, Object> error, String message, HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", message);
        responseBody.put("error", error);
        responseBody.put("success", false);

        return ResponseEntity.status(status).body(responseBody);
    }

    public static ResponseEntity<Map<String, Object>> buildNotFoundResponse(Map<String, Object> error) {
        return buildErrorResponse(error, DefaultResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<Map<String, Object>> buildNotFoundResponse() {
        return buildNotFoundResponse(null);
    }

    public static ResponseEntity<Map<String, Object>> buildInternalServerErrorResponse(Map<String, Object> error) {
        return buildErrorResponse(error, DefaultResponseMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<Map<String, Object>> buildInternalServerErrorResponse() {
        return buildInternalServerErrorResponse(null);
    }

    public static ResponseEntity<Map<String, Object>> buildBadRequestResponse(Map<String, Object> error) {
        return buildErrorResponse(error, DefaultResponseMessages.BAD_REQUEST, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<Map<String, Object>> buildBadRequestResponse(String message) {
        return buildErrorResponse(null, message, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<Map<String, Object>> buildBadRequestResponse() {
        return buildBadRequestResponse(DefaultResponseMessages.BAD_REQUEST);
    }

    public static ResponseEntity<Map<String, Object>> buildUnauthorizedResponse(Map<String, Object> error) {
        return buildErrorResponse(error, DefaultResponseMessages.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<Map<String, Object>> buildUnauthorizedResponse() {
        return buildUnauthorizedResponse(null);
    }
}

class DefaultResponseMessages {
    public static String REQUEST_SUCCESSFUL = "Request successful";
    public static String NOT_FOUND = "Request not found";
    public static String INTERNAL_SERVER_ERROR = "Something went wrong, please try again later.";
    public static String UNAUTHORIZED = "You are not authorized to access this resource.";
    public static String BAD_REQUEST = "Bad Request. Please try again.";
}