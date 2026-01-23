package com.ecommerce.user.service;

import java.util.Date;

public interface BlackListedTokenService {
    void markAsBlackListed(String token, Date expirationDate);
}
