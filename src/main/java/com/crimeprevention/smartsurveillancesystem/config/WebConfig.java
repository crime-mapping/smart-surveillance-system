package com.crimeprevention.smartsurveillancesystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Define the mapping for which CORS should be enabled
                .allowedOrigins("http://localhost:5173","https://crime-mapping.netlify.app") // Specify the allowed origins
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Specify the allowed HTTP methods
                .allowCredentials(true); // Allow cookies to be sent with the request
    }
}
