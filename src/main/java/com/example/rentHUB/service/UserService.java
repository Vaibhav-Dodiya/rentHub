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

    public void registerUser(String username,String email,String password){
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("Email already registered");
        }

        User user=new User();
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

   // @Autowired
   // private JavaMailSender mailSender;

   // private Map<String, String> otpStore = new HashMap<>();  // email -> OTP
  //  private Map<String, Long> otpExpiry = new HashMap<>();   // email -> timestamp

//    public boolean sendOtpToEmail(String email) {
//        Optional<User> userOpt = userRepository.findByEmail(email);
//        if (userOpt.isEmpty()) {
//            return false;
//        }
//
//        String otp = String.format("%06d", new Random().nextInt(999999));
//        otpStore.put(email, otp);
//        otpExpiry.put(email, System.currentTimeMillis() + 5 * 60 * 1000); // 5 min expiry
//
//        // Send email
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("Password Reset OTP - RentHub");
//        message.setText("Your OTP for password reset is: " + otp + "\n\nValid for 5 minutes.");
//        mailSender.send(message);
//
//        return true;
//    }
//
//    public boolean verifyOtpAndResetPassword(String email, String otp, String newPassword) {
//        if (!otpStore.containsKey(email)) return false;
//
//        String storedOtp = otpStore.get(email);
//        Long expiryTime = otpExpiry.get(email);
//
//        if (!storedOtp.equals(otp) || System.currentTimeMillis() > expiryTime) {
//            otpStore.remove(email);
//            otpExpiry.remove(email);
//            return false;
//        }
//
//        // Update password
//        User user = userRepository.findByEmail(email).orElseThrow();
//        user.setPassword(newPassword); // You can hash here
//        userRepository.save(user);
//
//        // Cleanup
//        otpStore.remove(email);
//        otpExpiry.remove(email);
//        return true;
//    }
}
