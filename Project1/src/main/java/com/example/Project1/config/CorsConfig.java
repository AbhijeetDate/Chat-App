package com.example.Project1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:5500",
                        "http://127.0.0.1:5501",

                        // Vercel production domain
                        "https://chat-app-frontend-chi-nine.vercel.app",

                        // Vercel deployment domains
                        "https://chat-app-frontend-git-main-abhijeetdate7-gmailcoms-projects.vercel.app",
                        "https://chat-app-frontend-bo4kwyqlg-abhijeetdate7-gmailcoms-projects.vercel.app"
                )
                .allowedMethods(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}