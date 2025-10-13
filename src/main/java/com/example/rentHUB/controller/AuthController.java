//package com.example.rentHUB.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import com.example.rentHUB.service.UserService;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/register")
//    public String register(){
//        return "register";
//    }
//    @PostMapping("/register")
//    @ResponseBody
//    public Response registerUser(@RequestParam String username,
//                               @RequestParam String email,
//                               @RequestParam String password){
//
//        try {
//            userService.registerUser(username, email, password);
//            return new Response("success", "User registered successfully");
//        } catch (Exception e) {
//            return new Response("error", e.getMessage());
//        }
//    }
//    @GetMapping("/login")
//    public String login(){
//        return "login";
//    }
//    @GetMapping("/welcome")
//    public String welcome(){
//    return "welcome";
//    }
//    static class Response {
//        private String status;
//        private String message;
//
//        public Response(String status, String message) {
//            this.status = status;
//            this.message = message;
//        }
//
//        public String getStatus() { return status; }
//        public String getMessage() { return message; }
//    }
//}
package com.example.rentHUB.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.rentHUB.service.UserService;

@RestController  // automatically adds @ResponseBody to all methods
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // POST /api/auth/register - accepts JSON body
    @PostMapping("/register")
    public Response registerUser(@RequestBody RegisterRequest request){
        try {
            userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
            return new Response("success", "User registered successfully");
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }

    // POST /api/auth/login placeholder
    @PostMapping("/login")
    public Response loginUser(@RequestBody LoginRequest request){
        // Implement login logic
        return new Response("success", "Login successful");
    }

    // DTO for registration JSON
    static class RegisterRequest {
        private String username;
        private String email;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // DTO for login JSON
    static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // Response DTO
    static class Response {
        private String status;
        private String message;

        public Response(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public String getStatus() { return status; }
        public String getMessage() { return message; }
    }
}
