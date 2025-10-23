package com.example.rentHUB.service;

import java.util.*;
import com.example.rentHUB.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.rentHUB.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    // private PasswordEncoder passwordEncoder;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//    public UserService(UserRepository userRepository){
//        this.userRepository=userRepository;
//    }

    public void registerUser(String username, String email, String password) {
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        userRepository.save(user);
    }

    public boolean loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false; // user not found
        }
        return passwordEncoder.matches(password, user.getPassword()); // check password
    }
}