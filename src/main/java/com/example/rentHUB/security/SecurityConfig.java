package com.example.rentHub.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(registry->{
                    registry.requestMatchers("/api/**").permitAll();
                    registry.requestMatchers("/req/**").permitAll();
                    registry.requestMatchers("/health").permitAll();
                    registry.anyRequest().authenticated();
                })
                .formLogin(httpForm ->{
                  httpForm.loginPage("/login").permitAll();
                })
                .build();
    }
}
