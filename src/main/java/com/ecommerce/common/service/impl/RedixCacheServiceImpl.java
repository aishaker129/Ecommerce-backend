package com.ecommerce.common.service.impl;

import com.ecommerce.common.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedixCacheServiceImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public <T> Optional<T> get(String key, Class<T> valueType) {
        Object cacheValue = getValue(key);
        if(Objects.isNull(cacheValue)){
            return Optional.empty();
        }
        return Optional.of(objectMapper.convertValue(cacheValue,valueType));
    }

    @Override
    public <T> List<T> getMany(String key, Class<T> itemValueType) {
        Object cacheValue = getValue(key);
        if(Objects.isNull(cacheValue)){
            return Collections.emptyList();
        }
        return objectMapper.convertValue(cacheValue,
                objectMapper.getTypeFactory().constructCollectionType(List.class,itemValueType));
    }

    @Override
    public void put(String key, Object value) {
        writeToCache(key,value,false,0L,null);
    }

    @Override
    public void putWithExpiry(String key, Object value, long timeout, TimeUnit unit) {

        writeToCache(key,value,true,timeout,unit);
    }

    @Override
    public void remove(String key) {

        redisTemplate.delete(key);
    }

    private Object getValue(String key){
        return redisTemplate.opsForValue().get(key);
    }

    private void writeToCache(String key, Object value,boolean withExpiry, Long timeout, TimeUnit unit){
        if(withExpiry){
            redisTemplate.opsForValue().set(key,value,timeout,unit);
        }
        else {
            redisTemplate.opsForValue().set(key,value);
        }
    }
}
