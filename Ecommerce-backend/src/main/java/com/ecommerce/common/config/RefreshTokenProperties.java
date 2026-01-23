package com.ecommerce.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.security.refresh-token")
@Data
public class RefreshTokenProperties {
    private long expirationMs;

    public long getExpirationSecond(){
        return expirationMs / 1000;
    }
}
