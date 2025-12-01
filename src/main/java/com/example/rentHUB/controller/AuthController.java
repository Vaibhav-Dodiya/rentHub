package com.example.rentHub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.rentHub.service.UserService;
import com.example.rentHub.model.User;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Admin secret key - change this to your own secret
    private static final String ADMIN_SECRET_KEY = "RENTHUB_ADMIN_2025";

    @PostMapping("/register")
    public Response registerUser(@RequestBody RegisterRequest request) {
        try {
            // Default to CUSTOMER if role not specified
            String role = request.getRole() != null ? request.getRole() : "CUSTOMER";
            
            // Validate admin secret key if registering as ADMIN
            if ("ADMIN".equalsIgnoreCase(role)) {
                String providedKey = request.getAdminSecretKey();
                if (providedKey == null || !ADMIN_SECRET_KEY.equals(providedKey)) {
                    return new Response("error", "Invalid admin secret key. Contact system administrator.");
                }
            }
            
            userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword(), role);
            return new Response("success", "User registered successfully");
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        User user = userService.authenticateUser(request.getEmail(), request.getPassword());
        if (user != null) {
            LoginResponse response = new LoginResponse("success", "Login successful", 
                user.getId(), user.getUsername(), user.getEmail(), user.getRole());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Response("error", "Invalid email or password"));
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(new String[]{"CUSTOMER", "OWNER", "ADMIN"});
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            String otp = userService.generatePasswordResetOTP(request.getEmail());
            if (otp != null) {
                // OTP sent via email
                return ResponseEntity.ok(new Response("success", "OTP has been sent to your email"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Response("error", "Email not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("error", e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Response> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            boolean success = userService.resetPassword(
                request.getEmail(), 
                request.getOtp(), 
                request.getNewPassword()
            );
            if (success) {
                return ResponseEntity.ok(new Response("success", "Password reset successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Response("error", "Invalid OTP or email"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("error", e.getMessage()));
        }
    }

    //

    static class RegisterRequest {
        private String username;
        private String email;
        private String password;
        private String role;
        private String adminSecretKey;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getAdminSecretKey() {
            return adminSecretKey;
        }

        public void setAdminSecretKey(String adminSecretKey) {
            this.adminSecretKey = adminSecretKey;
        }
    }

    static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    static class ForgotPasswordRequest {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    static class ResetPasswordRequest {
        private String email;
        private String otp;
        private String newPassword;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

    static class Response {
        private String status;
        private String message;

        public Response(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }

    static class LoginResponse extends Response {
        private String userId;
        private String username;
        private String email;
        private String role;

        public LoginResponse(String status, String message, String userId, String username, String email, String role) {
            super(status, message);
            this.userId = userId;
            this.username = username;
            this.email = email;
            this.role = role;
        }

        public String getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }
    }

    static class OtpResponse extends Response {
        private String otp;

        public OtpResponse(String status, String message, String otp) {
            super(status, message);
            this.otp = otp;
        }

        public String getOtp() {
            return otp;
        }
    }
}
