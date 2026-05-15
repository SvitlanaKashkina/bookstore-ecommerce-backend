package com.kashkina.bookstore.service;

import com.kashkina.bookstore.dto.UserDTO;
import com.kashkina.bookstore.entity.User;
import com.kashkina.bookstore.enums.Role;
import com.kashkina.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private static final Logger log =
            LoggerFactory.getLogger(AuthService.class);

    // REGISTER
    public User register(UserDTO dto) {

        log.info("Registering user with email={}", dto.getEmail());

        if (userRepository.existsByEmail(dto.getEmail())) {

            log.error("User already exists with email={}", dto.getEmail());

            throw new RuntimeException("User already exists");
        }

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .build();

        User saved = userRepository.save(user);

        log.info("User registered with id={}", saved.getId());

        return saved;
    }

    // LOGIN with AuthenticationManager
    public User login(String email, String password) {

        log.info("Login attempt for email={}", email);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        log.info("User successfully logged in: {}", email);

        return user;
    }
}
