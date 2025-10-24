////package com.example.rentHUB.config;
////
////public class CorsConfig {
////}
//package com.example.rentHUB.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.cors.CorsConfigurationSource;
//
//import java.util.Arrays;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("*")); // replace "*" with Flutter IP if needed
//        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration); // apply to all endpoints
//        return source;
//    }
//}
//package com.example.rentHUB.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.cors.CorsConfigurationSource;
//
//import java.util.Arrays;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        // 👇 Use the exact URL where your Flutter frontend runs
//        configuration.setAllowedOrigins(Arrays.asList("*"
////                "http://localhost:5353",
////                "http://127.0.0.1:5500",
////                "http://localhost:3000"
//        ));
//
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setAllowCredentials(false);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//}
//package com.example.rentHUB.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import java.util.Arrays;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.setAllowedOrigins(Arrays.asList(
//                "http://localhost:58364", // your Flutter Web origin
//                "https://rental-ar6cxohqg-yuvraj-singh-parmars-projects.vercel.app" // deployed Flutter app
//        ));
//        config.setAllowedHeaders(Arrays.asList("*"));
//        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }
//}
//
//package com.example.rentHUB.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import java.util.Arrays;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//
//        // Allow frontend URLs
//        config.setAllowedOrigins(Arrays.asList(
//                        "*"
//               // "http://localhost:58364", // Flutter web (local)
//              //  "https://rental-ar6cxohqg-yuvraj-singh-parmars-projects.vercel.app" // Flutter deployed app
//        ));
//
//        // Allow all common headers & methods
//        config.setAllowedHeaders(Arrays.asList("*"));
//        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//
//        // Allow cookies/authorization if used
//        config.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//
//        return new CorsFilter(source);
//    }
//}

package com.example.rentHUB.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Define all allowed origins for your frontend
        // If you are not using session cookies, set allowCredentials(false) for safety.
        List<String> allowedOrigins = Arrays.asList(
                // Production deployment (Vercel, etc.)
                "https://rental-ar6cxohqg-yuvraj-singh-parmars-projects.vercel.app",

                // Flutter Web default local port
                "http://localhost:58364",

                // Other common local development ports (React/Angular/Vue/mobile emulators)
                "http://localhost:3000",
                "http://localhost:8080",
                "http://127.0.0.1:8080",

                // If testing on a mobile emulator, you might need its local IP address or '10.0.2.2' (Android emulator loopback)
                "http://10.0.2.2:8080"
        );

        registry.addMapping("/**") // Apply CORS configuration to all paths
                .allowedOrigins(allowedOrigins.toArray(new String[0]))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(false); // Set to false unless you are explicitly relying on session cookies for authentication.
    }
}
