package com.kashkina.bookstore.security.config;

import com.kashkina.bookstore.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // PUBLIC ENDPOINTS
                        // =========================

                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET,
                                "/api/books/all",
                                "/api/books/*",
                                "/api/books/search/title",
                                "/api/books/search/category"
                        ).permitAll()

                        // =========================
                        // ADMIN ENDPOINTS
                        // =========================

                        .requestMatchers(HttpMethod.POST,
                                "/api/books/create"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/books/{id}"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,
                                "/api/users"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,
                                "/api/payments"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,
                                "/api/payment-details"
                        ).hasRole("ADMIN")

                        // =========================
                        // USER ENDPOINTS
                        // =========================

                        .requestMatchers("/api/users/**")
                        .authenticated()

                        .requestMatchers("/api/carts/**")
                        .authenticated()

                        .requestMatchers("/api/orders/**")
                        .authenticated()

                        .requestMatchers("/api/order-items/**")
                        .authenticated()

                        .requestMatchers("/api/payments/**")
                        .authenticated()

                        .requestMatchers("/api/payment-details/**")
                        .authenticated()

                        // =========================

                        .anyRequest().authenticated()
                )

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
