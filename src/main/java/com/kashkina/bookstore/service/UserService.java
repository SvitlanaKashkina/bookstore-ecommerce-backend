package com.kashkina.bookstore.service;

import com.kashkina.bookstore.dto.UserDTO;
import com.kashkina.bookstore.entity.User;
import com.kashkina.bookstore.exception.ResourceNotFoundException;
import com.kashkina.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    // Get user by ID
    public UserDTO getUserById(Long id) {
        log.info("Fetching user by id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id={}", id);
                    return new ResourceNotFoundException("User not found with id=" + id);
                });

        return mapToDTO(user);
    }

    // Get user by email
    public UserDTO getUserByEmail(String email) {
        log.info("Fetching user by email={}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found with email={}", email);
                    return new ResourceNotFoundException("User not found with email=" + email);
                });

        return mapToDTO(user);
    }

    // Get all users (ADMIN)
    public List<UserDTO> getAllUsers() {
        log.info("Fetching all users");

        List<UserDTO> users = userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        log.info("Total users found: {}", users.size());
        return users;
    }

    // Update user
    public UserDTO updateUser(Long id, UserDTO dto) {
        log.info("Updating user with id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id={}", id);
                    return new ResourceNotFoundException("User not found with id=" + id);
                });

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());

        User updated = userRepository.save(user);

        log.info("User updated with id={}", updated.getId());
        return mapToDTO(updated);
    }

    // Delete user
    public void deleteUser(Long id) {
        log.info("Deleting user with id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id={}", id);
                    return new ResourceNotFoundException("User not found with id=" + id);
                });

        userRepository.delete(user);
        log.info("User deleted with id={}", id);
    }

    // Mapper
    private UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
