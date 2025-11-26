package com.example.rentHub.service;

import com.example.rentHub.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.rentHub.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired(required = false)
    private EmailService emailService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // Simple in-memory OTP storage (use Redis or database in production)
    private Map<String, String> otpStorage = new HashMap<>();
    private Map<String, LocalDateTime> otpExpiryStorage = new HashMap<>();
    private static final int OTP_VALIDITY_MINUTES = 10;

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

    public String generatePasswordResetOTP(String email) {
        System.out.println("Looking for user with email: " + email);
        User user = userRepository.findByEmail(email);
        System.out.println("User found: " + (user != null ? user.getEmail() : "null"));
        if (user == null) {
            return null;
        }
        
        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);
        otpExpiryStorage.put(email, LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES));
        
        // Send OTP via email
        try {
            if (emailService != null) {
                emailService.sendOtpEmail(email, otp);
                System.out.println("OTP email sent successfully to: " + email);
            } else {
                System.err.println("Email service not configured - OTP: " + otp);
            }
        } catch (Exception e) {
            // If email fails, still return OTP for testing/debugging
            System.err.println("Failed to send email: " + e.getMessage());
            System.err.println("OTP for " + email + ": " + otp);
        }
        
        return otp;
    }

    public boolean resetPassword(String email, String otp, String newPassword) {
        String storedOtp = otpStorage.get(email);
        LocalDateTime expiryTime = otpExpiryStorage.get(email);
        
        // Check if OTP exists
        if (storedOtp == null || !storedOtp.equals(otp)) {
            return false;
        }
        
        // Check if OTP has expired
        if (expiryTime == null || LocalDateTime.now().isAfter(expiryTime)) {
            otpStorage.remove(email);
            otpExpiryStorage.remove(email);
            return false;
        }
        
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        // Remove used OTP
        otpStorage.remove(email);
        otpExpiryStorage.remove(email);
        
        return true;
    }
}
