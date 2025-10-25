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
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                // Disable CSRF for APIs
//                .csrf(AbstractHttpConfigurer::disable)
//
//                // Enable CORS (for frontend connections)
//                .cors(cors -> cors.configurationSource(request -> {
//                    var corsConfig = new CorsConfiguration();
//                     corsConfig.setAllowedOrigins(List.of("*"
////                    "https://rental-ar6cxohqg-yuvraj-singh-parmars-projects.vercel.app",
////                            "http://localhost:3000",               // ✅ local dev frontend
////                            "http://localhost:8080",
////                            "http://localhost:58364"
//                    ));
//                    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//                    corsConfig.setAllowedHeaders(List.of("*"));
//                    corsConfig.setAllowCredentials(true);
//                    return corsConfig;
//                }))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/","/health", "/index.html", "/static/**", "/public/**","/auth/**","/api/users/**", "/api/auth/**", "/register", "/login", "/welcome").permitAll()
//                        .anyRequest().authenticated())
//
//                // Stateless session (for APIs)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//
//                // Disable default login/logout/basic
//                .formLogin(AbstractHttpConfigurer::disable)
//                .logout(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable);
//
//        return http.build();
//    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}

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

    // Comma-separated list of allowed origins for production.
    // Example (Render env var):
    // https://your-frontend.vercel.app,https://your-domain.com
    // Default "*" allows any origin (useful for local dev and quick checks).
    @Value("${app.cors.allowed-origins:*}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new CorsConfiguration();
                    // Support both explicit origin list and wildcard patterns via env var
                    if (allowedOrigins != null && !allowedOrigins.isBlank() && !"*".equals(allowedOrigins.trim())) {
                        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .toList();
                        config.setAllowedOrigins(origins);
                    } else {
                        // Wildcard pattern allows Spring to echo back the Origin when credentials are
                        // enabled
                        config.setAllowedOriginPatterns(List.of("*"));
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
                                "/public/**", "/static/**", "/health","/api/users/**","/auth/**")
                        .permitAll()
                        .anyRequest().authenticated())
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
