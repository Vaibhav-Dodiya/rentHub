package com.example.rentHub.service;

import com.example.rentHub.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.rentHub.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void registerUser(String username, String email, String password, String role) {
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("Email already registered");
        }

        // Validate role
        if (role == null || role.isEmpty()) {
            role = "CUSTOMER";
        }
        if (!role.equals("CUSTOMER") && !role.equals("OWNER") && !role.equals("ADMIN")) {
            throw new RuntimeException("Invalid role. Must be CUSTOMER, OWNER, or ADMIN");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        userRepository.save(user);
    }

    public boolean loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPassword());
    }

    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
