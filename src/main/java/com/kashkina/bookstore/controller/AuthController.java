package com.kashkina.bookstore.controller;

import com.kashkina.bookstore.dto.LoginRequest;
import com.kashkina.bookstore.dto.LoginResponse;
import com.kashkina.bookstore.dto.UserDTO;
import com.kashkina.bookstore.entity.User;
import com.kashkina.bookstore.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private static final Logger log =
            LoggerFactory.getLogger(AuthController.class);

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<User> register(
            @Valid @RequestBody UserDTO dto
    ) {
        log.info("HTTP POST /auth/register");

        User user = authService.register(dto);

        return ResponseEntity.ok(user);
    }

    // LOGIN (IMPORTANT FIXED)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {
        log.info("HTTP POST /auth/login for email={}", request.getEmail());

        authService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(
                new LoginResponse("Login successful")
        );
    }
}
