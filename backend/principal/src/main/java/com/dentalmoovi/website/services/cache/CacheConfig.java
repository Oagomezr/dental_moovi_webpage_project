package com.dentalmoovi.website.services.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    @Primary
    public Cache<String, String> registrationCodeConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(20, TimeUnit.MINUTES) // Set expiration time
                .maximumSize(1000) // Set the max cache's size
                .build();
    }

    @Bean
    public Cache<String, String> replayCodeRestrict() {
        return Caffeine.newBuilder()
                .expireAfterWrite(50, TimeUnit.SECONDS) // Set expiration time
                .maximumSize(1000) // Set the max cache's size
                .build();
    }

    @Bean
    public Cache<String, Integer> numberOfTriesCode() {
        return Caffeine.newBuilder()
                .expireAfterWrite(20, TimeUnit.MINUTES) // Set expiration time
                .maximumSize(1000) // Set the max cache's size
                .build();
    }
}
