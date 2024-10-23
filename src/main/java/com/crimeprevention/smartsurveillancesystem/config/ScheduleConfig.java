package com.crimeprevention.smartsurveillancesystem.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import com.crimeprevention.smartsurveillancesystem.services.VerificationCodeService;

@Configuration
@EnableScheduling
public class ScheduleConfig {

    private final VerificationCodeService verificationCodeService;

    public ScheduleConfig(VerificationCodeService verificationCodeService) {
        this.verificationCodeService = verificationCodeService;
    }

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    public void cleanupExpiredCodes() {
        verificationCodeService.cleanupExpiredCodes();
    }
}
