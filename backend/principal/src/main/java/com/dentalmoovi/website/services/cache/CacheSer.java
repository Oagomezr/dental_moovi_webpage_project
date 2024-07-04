package com.dentalmoovi.website.services.cache;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;

@Service
public class CacheSer {
    private Cache<String, String> registrationCodeConfig;
    private Cache<String, String> replayCodeRestrict;
    private Cache<String, Integer> numberOfTriesCode;

    public CacheSer(    
        @Qualifier("registrationCodeConfig") Cache<String, String> registrationCodeConfig, 
        @Qualifier("replayCodeRestrict") Cache<String, String> replayCodeRestrict,
        @Qualifier("numberOfTriesCode") Cache<String, Integer> numberOfTriesCode
    ) {
        this.registrationCodeConfig = registrationCodeConfig;
        this.replayCodeRestrict = replayCodeRestrict;
        this.numberOfTriesCode = numberOfTriesCode;
    }

    public void addToOrUpdateRegistrationCache(String key, String value) {
        registrationCodeConfig.put(key, value);
    }

    public String getFromRegistrationCache(String key) {
        return registrationCodeConfig.getIfPresent(key);
    }

    public void removeFromRegistrationCache(String key) {
        registrationCodeConfig.invalidate(key);
    }

    public void addToOrUpdateReplayCodeRestrict(String key, String value) {
        replayCodeRestrict.put(key, value);
    }

    public String getFromReplayCodeRestrict(String key) {
        return replayCodeRestrict.getIfPresent(key);
    }

    public void addToOrUpdateNumberTries(String key, Integer value) {
        numberOfTriesCode.put(key, value);
    }

    public Integer getFromNumberTries(String key) {
        return numberOfTriesCode.getIfPresent(key);
    }

    public void removeFromNumberTries(String key) {
        numberOfTriesCode.invalidate(key);
    }
}
