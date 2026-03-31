package com.kashkina.bookstore.controller;

import com.kashkina.bookstore.dto.UserDTO;
import com.kashkina.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    // Get user by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO getUserById(@PathVariable Long id) {
        log.info("HTTP GET /users/{} - Get user by ID", id);
        return userService.getUserById(id);
    }

    // Get user by email
    @GetMapping("/by-email")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO getUserByEmail(@RequestParam String email) {
        log.info("HTTP GET /users/by-email?email={} - Get user by email", email);
        return userService.getUserByEmail(email);
    }

    // Get all users (ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllUsers() {
        log.info("HTTP GET /users - Get all users");
        return userService.getAllUsers();
    }

    // Update user
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        log.info("HTTP PUT /users/{} - Update user", id);
        return userService.updateUser(id, dto);
    }

    // Delete user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        log.info("HTTP DELETE /users/{} - Delete user", id);
        userService.deleteUser(id);
    }
}
