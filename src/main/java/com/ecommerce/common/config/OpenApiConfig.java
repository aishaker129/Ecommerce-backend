package com.ecommerce.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Value("${APP_BASE_URL:http://localhost:8081}")
    private String baseUrl;

    @Bean
    public OpenAPI apiDocumentation(){
        return new OpenAPI()
                .servers(List.of(new Server().url(baseUrl)))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("CampusKart: University Based Ecommerce System.")
                .description("Backend service for University based ecommerce platfrom using java spring boot.")
                .version("1.0")
                .contact(apiContact())
                .license(apiLicence());
    }

    private License apiLicence() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }

    private Contact apiContact() {
        return new Contact()
                .name("Md. Akhlakul Islam.")
                .email("aishaker129@gmail.com");
    }
}
