package com.example.rentHub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${app.email.from:noreply@renthub.com}")
    private String fromEmail;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("RentHub - Password Reset OTP");
            message.setText(buildOtpEmailBody(otp));
            
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    private String buildOtpEmailBody(String otp) {
        return "Hello,\n\n" +
               "You requested to reset your password for your RentHub account.\n\n" +
               "Your OTP (One-Time Password) is: " + otp + "\n\n" +
               "This OTP is valid for 10 minutes.\n\n" +
               "If you did not request this password reset, please ignore this email.\n\n" +
               "Best regards,\n" +
               "RentHub Team";
    }
}
