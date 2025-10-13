package com.example.rentHUB.service;

import com.example.rentHUB.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.rentHUB.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    public void registerUser(String username,String email,String password){
        User user=new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        userRepository.save(user);
    }

}
