package com.ecommerce.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiDocumentation() {
        return new OpenAPI()
                .info(apiInfo());
    }

    private Info apiInfo(){
        return new Info()
                .title("Ecommerce Backend Service")
                .description("Backend service for Ecommerce platform using spring boot")
                .version("1.0")
                .contact(apiConcate())
                .license(apiLicense());
    }

    private Contact apiConcate(){
        return new Contact()
                .name("Md. Akhlakul Islam")
                .email("aishaker129@gmail.com");
    }

    private License apiLicense(){
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }
}
