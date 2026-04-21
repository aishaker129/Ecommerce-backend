package com.ecommerce.payment.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "stripe")
@Data
public class StripeConfig {
    private String apiKey;
    private String successUrl;
    private String cancelUrl;
    private String currency;

    @PostConstruct
    public void init(){
        Stripe.apiKey = apiKey;
    }
}
