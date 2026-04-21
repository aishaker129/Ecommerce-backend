package com.ecommerce.common.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface CacheService {
    <T>Optional<T> get(String  key, Class<T> valueType);

    <T>List<T> getMany(String key, Class<T> itemValueType);

    void put(String key, Object value);

    void putWithExpiry(String key, Object value, long timeout, TimeUnit unit);

    void remove(String key);
}
