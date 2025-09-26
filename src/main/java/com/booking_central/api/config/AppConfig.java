package com.booking_central.api.config;

public class AppConfig {
    // Jwt Config
    public static final String jwtPrivateKey = "${app.security.jwt.key.private}";
    public static final String jwtPublicKey = "${app.security.jwt.key.public}";
    public static final String jwtAccessExpiration = "${app.security.jwt.expiration.access}";
    public static final String jwtRefreshExpiration = "${app.security.jwt.expiration.refresh}";
}
