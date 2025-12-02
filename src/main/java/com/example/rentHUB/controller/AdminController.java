package com.example.rentHub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.rentHub.repository.UserRepository;
import com.example.rentHub.repository.PropertyRepository;
import com.example.rentHub.model.User;
import com.example.rentHub.model.Property;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PropertyRepository propertyRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            if (!userRepository.existsById(id)) {
                return ResponseEntity.status(404).body("User not found");
            }
            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting user: " + e.getMessage());
        }
    }
    
    @GetMapping("/posts")
    public ResponseEntity<List<Property>> getAllPosts() {
        List<Property> properties = propertyRepository.findAll();
        return ResponseEntity.ok(properties);
    }
    
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        try {
            if (!propertyRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Post not found");
            }
            propertyRepository.deleteById(id);
            return ResponseEntity.ok("Post deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting post: " + e.getMessage());
        }
    }
}
