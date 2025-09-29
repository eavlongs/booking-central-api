package com.booking_central.api.security;

import com.booking_central.api.config.AppConfig;
import com.booking_central.api.dto.JwtClaim;
import com.booking_central.api.enums.JwtType;
import com.booking_central.api.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Service
public class JwtService {
    private PrivateKey privateKey;
    private RSAPublicKey publicKey;
    @Value(AppConfig.jwtAccessExpiration)
    private String accessTokenExpirationInMinutes;
    @Value(AppConfig.jwtRefreshExpiration)
    private String refreshTokenExpirationInMinutes;
    @Value(AppConfig.jwtAuthRequestExpiration)
    private String authRequestTokenExpirationInMinutes;

    // RS256 Algorithm implementation
    // https://medium.com/@srivastp/securing-jwt-authentication-in-spring-boot-with-rsa-keys-666b5c467378
    public JwtService(@Value(AppConfig.jwtPrivateKey) String privateKeyPath,
                      @Value(AppConfig.jwtPublicKey) String publicKeyPath) throws IOException, NoSuchAlgorithmException
            , InvalidKeySpecException {

        this.privateKey = loadPrivateKey(privateKeyPath);
        this.publicKey = loadPublicKey(publicKeyPath);
    }

    private static PrivateKey loadPrivateKey(String keyPath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String key = loadKeyFromPath(keyPath);
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    private static RSAPublicKey loadPublicKey(String keyPath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String key = loadKeyFromPath(keyPath);
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(spec);
    }

    private static String loadKeyFromPath(String keyPath) throws IOException {
        String content;

        // Try to load as classpath resource first
        try {
            Resource resource = new ClassPathResource(keyPath);
            try (InputStream inputStream = resource.getInputStream()) {
                content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            // Fallback to file system path
            content = Files.readString(Paths.get(keyPath));
        }

        // Clean up the key content
        return content
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");
    }

    public String buildToken(User user, JwtType jwtType) {
        return buildToken(user, jwtType, Map.of());
    }

    public String buildToken(User user, JwtType jwtType, Map<String, Object> additionalClaims) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("first_name", user.getFirstName());
        claims.put("last_name", user.getLastName());
        claims.put("type", jwtType.toString());
        claims.putAll(additionalClaims);

        Date expiration;

        if (jwtType == JwtType.ACCESS_TOKEN) {
            expiration = new Date(System.currentTimeMillis() + (long) (Double.parseDouble(accessTokenExpirationInMinutes) * 60 * 1000));
        } else if (jwtType == JwtType.REFRESH_TOKEN) {
            expiration = new Date(System.currentTimeMillis() + (long) (Double.parseDouble(refreshTokenExpirationInMinutes) * 60 * 1000));
        } else if (jwtType == JwtType.AUTH_REQUEST) {
            expiration = new Date(System.currentTimeMillis() + (long) (Double.parseDouble(authRequestTokenExpirationInMinutes) * 60 * 1000));
        } else {
            throw new IllegalArgumentException("Invalid JWT type");
        }

        return Jwts
                .builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiration)
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public JwtClaim extractJwtClaim(String token) throws IllegalArgumentException, NullPointerException {
        Claims claims = extractClaims(token);
        System.out.println(claims);
        return new JwtClaim(claims);
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}