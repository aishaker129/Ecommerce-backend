package com.ecommerce.user.services;

import java.util.Date;

public interface BlackListedTokenService {
    void markBlackListed(String token, Date expirationDate);

    boolean isBlackListed(String token);
}
