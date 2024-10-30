package com.crimeprevention.smartsurveillancesystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Define the mapping for which CORS should be enabled
                .allowedOrigins("http://localhost:3000","https://smart-surveillance-system.onrender.com/") // Specify the allowed origins
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Specify the allowed HTTP methods
                .allowCredentials(true); // Allow cookies to be sent with the request
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/api/**") // Specify protected paths
                .excludePathPatterns("/api/auth/register", "/api/auth/login","/api/auth/factor"); // Exclude non-protected paths
    }
}
