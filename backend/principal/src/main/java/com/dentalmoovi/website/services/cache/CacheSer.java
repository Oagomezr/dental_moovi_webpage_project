package com.dentalmoovi.website.services.cache;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;

@Service
public class CacheSer {
    private Cache<String, Integer> numberOfTriesCode;

    public CacheSer(@Qualifier("numberOfTriesCode") Cache<String, Integer> numberOfTriesCode) {
        this.numberOfTriesCode = numberOfTriesCode;
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
