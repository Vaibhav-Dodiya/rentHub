package com.example.rentHUB.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.rentHUB.service.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {
        "https://rental-hub-lake.vercel.app",
        "http://localhost:8080",
        "http://localhost:3000"
})
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Response registerUser(@RequestBody RegisterRequest request){
        try {
            userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
            return new Response("success", "User registered successfully");
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody LoginRequest request) {
        boolean success = userService.loginUser(request.getEmail(), request.getPassword());
        if (success) {
            return ResponseEntity.ok(new Response("success", "Login successful"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Response("error", "Invalid email or password"));
        }
    }

    //    @PostMapping("/forgot-password")
//    public Response forgotPassword(@RequestBody ForgotPasswordRequest request) {
//        try {
//            boolean otpSent = userService.sendOtpToEmail(request.getEmail());
//            if (otpSent) {
//                return new Response("success", "OTP sent to your registered email");
//            } else {
//                return new Response("error", "Email not registered");
//            }
//        } catch (Exception e) {
//            return new Response("error", "Something went wrong: " + e.getMessage());
//        }
//    }
//
//    // ✅ Step 2: Verify OTP & Reset Password
//    @PostMapping("/reset-password")
//    public Response resetPassword(@RequestBody ResetPasswordRequest request) {
//        try {
//            boolean reset = userService.verifyOtpAndResetPassword(
//                    request.getEmail(), request.getOtp(), request.getNewPassword());
//            if (reset) {
//                return new Response("success", "Password reset successfully");
//            } else {
//                return new Response("error", "Invalid OTP or expired");
//            }
//        } catch (Exception e) {
//            return new Response("error", "Something went wrong: " + e.getMessage());
//        }
//    }
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
    static class ForgotPasswordRequest {
        private String email;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
    static class ResetPasswordRequest {
        private String email;
        private String otp;
        private String newPassword;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getOtp() { return otp; }
        public void setOtp(String otp) { this.otp = otp; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
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
