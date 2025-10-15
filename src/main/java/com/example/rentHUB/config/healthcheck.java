package com.example.rentHUB.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class healthcheck {

    @GetMapping("/health")
    public String healthCheck() {
        return "Application is running!";
    }
}
