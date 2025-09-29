package com.booking_central.api.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

// https://medium.com/javarevisited/caffeine-cache-f106cee91925
@Configuration
@EnableCaching
public class CacheConfig {
    @Value(AppConfig.caffeineInitialCapacity)
    private int initialCapacity;
    @Value(AppConfig.caffeineMaximumSize)
    private int maximumSize;
    @Value(AppConfig.caffeineExpiryTime)
    private int expiry; // in minutes
    @Value("#{'" + AppConfig.caffeineCacheNames + "'.split(',')}") // full form is #{'${...}'.split(',')}
    private List<String> cacheNames;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(cacheNames.toArray(new String[0]));
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
            .initialCapacity(initialCapacity)
                .maximumSize(maximumSize)
                .expireAfterWrite(Duration.ofMinutes(expiry));
    }
}
