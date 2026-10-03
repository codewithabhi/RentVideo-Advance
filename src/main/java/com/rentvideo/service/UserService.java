package com.rentvideo.service;

import com.rentvideo.domain.Role;
import com.rentvideo.domain.User;
import com.rentvideo.exception.BusinessException;
import com.rentvideo.exception.ResourceNotFoundException;
import com.rentvideo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {
        if (user == null) {
            throw new BusinessException("User details are required");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new BusinessException("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new BusinessException("Password is required");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessException("User with this email already exists");
        }

        user.setRole(user.getRole() == null ? Role.CUSTOMER : user.getRole());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for email: " + email));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
