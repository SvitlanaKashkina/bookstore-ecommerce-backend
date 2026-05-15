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

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // PUBLIC
                        .requestMatchers("/api/auth/**")
                        .permitAll()

                        .requestMatchers(HttpMethod.GET,
                                "/api/books/all",
                                "/api/books/*",
                                "/api/books/search/title",
                                "/api/books/search/category"
                        ).permitAll()

                        // ADMIN
                        .requestMatchers(HttpMethod.POST,
                                "/api/books/create"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/books/{id}"
                        ).hasRole("ADMIN")

                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        // AUTHENTICATED USER
                        .requestMatchers("/api/users/**")
                        .authenticated()

                        .requestMatchers("/api/carts/**")
                        .authenticated()

                        .requestMatchers("/api/orders/**")
                        .authenticated()

                        .anyRequest()
                        .authenticated()
                )

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
