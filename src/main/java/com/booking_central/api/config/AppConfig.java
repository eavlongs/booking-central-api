package com.booking_central.api.config;

public class AppConfig {
    // Jwt Config
    public static final String jwtPrivateKey = "${app.security.jwt.key.private}";
    public static final String jwtPublicKey = "${app.security.jwt.key.public}";
    public static final String jwtAccessExpiration = "${app.security.jwt.expiration.access}";
    public static final String jwtRefreshExpiration = "${app.security.jwt.expiration.refresh}";
    public static final String jwtAuthRequestExpiration = "${app.security.jwt.expiration.auth-request}";

    public static final String frontEndUrl = "${app.url.frontend}";
    public static final String oauth2SuccessUrl = "${app.url.oauth2-success}";
    public static final String oauth2FailureUrl = "${app.url.oauth2-failure}";

    public static final String caffeineCacheNames = "${app.cache.caffeine.names}";
    public static final String caffeineInitialCapacity = "${app.cache.caffeine.initial-capacity}";
    public static final String caffeineMaximumSize = "${app.cache.caffeine.maximum-size}";
    public static final String caffeineExpiryTime = "${app.cache.caffeine.expiry-time}"; // in minutes
}
