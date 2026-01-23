package com.ecommerce.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKey {
    BLACK_LISTED_TOKEN("auth.blacklisted.%s");

    private final String key;

    public String formate(Object... args){
        return String.format(key,args);
    }
}
