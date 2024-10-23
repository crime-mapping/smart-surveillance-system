package com.crimeprevention.smartsurveillancesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartSurveillanceSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartSurveillanceSystemApplication.class, args);
    }

}
