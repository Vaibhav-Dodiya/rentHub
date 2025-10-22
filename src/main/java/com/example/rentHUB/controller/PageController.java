package com.example.rentHUB.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class PageController {
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // looks for login.html in templates/
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // looks for register.html in templates/
    }

    @GetMapping("/welcome")
    public String welcomePage() {
        return "welcome"; // looks for welcome.html in templates/
    }
}
