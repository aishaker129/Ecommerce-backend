package com.ecommerce.user.services.impl;

import com.ecommerce.common.enums.RedisKey;
import com.ecommerce.common.service.CacheService;
import com.ecommerce.user.services.BlackListedTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BlackListedTokenServiceImpl implements BlackListedTokenService {
    private final CacheService cacheService;

    @Override
    public void markBlackListed(String token, Date expirationDate) {

        String redisKey = getRedisKey(token);
        long expiryDurationInMs = expirationDate.getTime() - System.currentTimeMillis();
        cacheService.putWithExpiry(redisKey,token,expiryDurationInMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isBlackListed(String token) {
        String redisKey = getRedisKey(token);
        return cacheService.get(redisKey, String.class).isPresent(); // ✅
    }

    private String getRedisKey(String token) {
        return RedisKey.BLACKLISTED_TOKEN.format(token);
    }
}
