package com.ecommerce.user.service.impl;

import com.ecommerce.common.enums.RedisKey;
import com.ecommerce.common.service.CacheService;
import com.ecommerce.user.service.BlackListedTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BlackListedTokenServiceImpl implements BlackListedTokenService {
    private final CacheService cacheService;
    @Override
    public void markAsBlackListed(String token, Date expirationDate) {
        String redisKey = getRedisKey(token);
        long expirationDurationInMs = expirationDate.getTime() - System.currentTimeMillis();

        cacheService.putWithExpiry(redisKey,token,expirationDurationInMs, TimeUnit.MILLISECONDS);
    }

    private String getRedisKey(String token) {
        return RedisKey.BLACK_LISTED_TOKEN.formate(token);
    }
}
