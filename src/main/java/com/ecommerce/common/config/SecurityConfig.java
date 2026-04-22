package com.ecommerce.common.config;

import com.ecommerce.common.constants.ApiEndPoints;
import com.ecommerce.common.filter.JwtAuthenticationFilter;
import com.ecommerce.user.enums.RoleType;
import com.ecommerce.user.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] WHITELIST_URLS = {
            "/error/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/actuator/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/api/v1/webhook/**",
            "/api/v1/payments/success",
            "/api/v1/payments/cancel",
            "/dummy-success",
            ApiEndPoints.Auth.BASE_AUTH + ApiEndPoints.Auth.LOGIN,
            ApiEndPoints.Auth.BASE_AUTH + ApiEndPoints.Auth.REGISTER,
            ApiEndPoints.Auth.BASE_AUTH + ApiEndPoints.Auth.LOGOUT,
            ApiEndPoints.Auth.BASE_AUTH + ApiEndPoints.Auth.TOKEN_REFRESH,
            ApiEndPoints.ProductAdmin.BASE_PRODUCT + ApiEndPoints.ProductAdmin.PRODUCT_DETAILS,
            ApiEndPoints.ProductAdmin.BASE_PRODUCT + ApiEndPoints.ProductAdmin.ALL_PRODUCT,
            ApiEndPoints.ProductAdmin.BASE_PRODUCT + ApiEndPoints.ProductAdmin.CHECK_PRODUCT_STOCK
    };

    private final ObjectMapper objectMapper;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); // or specify your frontend URL
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public AuthenticationProvider authenticationProvider(UserService userService, PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   AuthenticationProvider authenticationProvider,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception{

        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers(WHITELIST_URLS).permitAll()
                                .requestMatchers("/api/v1/admin/**").hasAnyRole(RoleType.ADMIN.name(),RoleType.SUPER_ADMIN.name())
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            objectMapper.writeValue(response.getWriter(),
                                    ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                                            "Authentication required. Provide a valid JWT token."));
                        })
                        .accessDeniedHandler((request, response, e) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            objectMapper.writeValue(response.getWriter(),
                                    ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,
                                            "You do not have permission to access this resource."));
                        })
                );

        return httpSecurity.build();
    }
}
