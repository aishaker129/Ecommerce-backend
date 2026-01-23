package com.ecommerce.common.config;

import com.ecommerce.common.constants.ApiEndpoint;
import com.ecommerce.common.filter.JwtAuthenticationFilter;
import com.ecommerce.user.enums.RoleType;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] WITELIST_URLS =  {
            "/error/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            ApiEndpoint.Auth.BASE_AUTH + ApiEndpoint.Auth.REGISTER,
            ApiEndpoint.Auth.BASE_AUTH + ApiEndpoint.Auth.LOGIN,
            ApiEndpoint.Auth.BASE_AUTH + ApiEndpoint.Auth.LOGOUT,
            ApiEndpoint.Auth.BASE_AUTH + ApiEndpoint.Auth.TOKEN_REFRESH
    };

    private final ObjectMapper objectMapper;

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                                           AuthenticationProvider authenticationProvider,
                                           JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        httpSecurity
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request-> request
                        // public endpoints here
                                .requestMatchers(WITELIST_URLS).permitAll()

                        // Admin endpoints here
                                .requestMatchers("/api/v1/admin/**").hasAnyRole(RoleType.SUPER_ADMIN.name(),RoleType.ADMIN.name())

                        // Other endpoints required authentication
                                .anyRequest().authenticated()
                )
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception->exception
                        .accessDeniedHandler(((request, response, _) -> {
                            // Customizing error message in case of permission denied
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,
                                    "You do not have permission to access this resource.");
                            problemDetail.setProperty("path", request.getRequestURI());

                            objectMapper.writeValue(response.getWriter(), problemDetail);
                        }))
                );
        return httpSecurity.build();
    }
}
