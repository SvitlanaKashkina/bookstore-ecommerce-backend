package com.kashkina.bookstore.controller;

import com.kashkina.bookstore.dto.UserDTO;
import com.kashkina.bookstore.entity.User;
import com.kashkina.bookstore.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    // REGISTER
    @PostMapping("/register")
    public User register(@Valid @RequestBody UserDTO dto) {
        log.info("HTTP POST /auth/register");
        return authService.register(dto);
    }

    // LOGIN
    @PostMapping("/login")
    public User login(@RequestParam String email,
                      @RequestParam String password) {
        log.info("HTTP POST /auth/login for email={}", email);
        return authService.login(email, password);
    }
}
