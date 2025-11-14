<<<<<<< HEAD
=======
//package com.example.rentHUB.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//import com.example.rentHUB.service.CustomUserDetails;
//import org.springframework.web.cors.CorsConfiguration;
//
//import java.util.List;
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {

//

//
//
//
//

//

//

//

//}

>>>>>>> f9c9e4b85c89a466e6c16a50e8c122d86476610e
package com.example.rentHUB.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.cors.allowed-origins:*}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new CorsConfiguration();

                    if (allowedOrigins != null && !allowedOrigins.isBlank() && !"*".equals(allowedOrigins.trim())) {
                        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .toList();
                        config.setAllowedOrigins(origins);
                    } else {
<<<<<<< HEAD
                        // Wildcard pattern allows Spring to echo back the Origin when credentials are
                        // enabled
                        //config.setAllowedOriginPatterns(List.of("*"));
                        config.setAllowedOriginPatterns(List.of("http://localhost:58364",
                                "https://rental-hub-lake.vercel.app")); // dev + prod

=======

                        config.setAllowedOriginPatterns(List.of("*"));
>>>>>>> f9c9e4b85c89a466e6c16a50e8c122d86476610e
                    }
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setExposedHeaders(List.of("Authorization", "Content-Type"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/", "/auth/**", "/register", "/login",
                                "/public/**", "/static/**", "/health",
                                "/api/users/**","/auth/**")
                        .permitAll()
                        .anyRequest().authenticated())
                //.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
