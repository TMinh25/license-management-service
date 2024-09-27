package com.fpt.fis.license_management.configuration;

import com.fpt.fis.license_management.jobs.LicenseRequest113;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Value("${job.constants.license-request-113.cron}") String licenseRequest113CronExpression;

    @Bean
    public JobDetail licenseRequest113JobDetail() {
        return JobBuilder.newJob(LicenseRequest113.class)
                .withIdentity("LicenseRequest113")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger licenseRequest113JobTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(licenseRequest113CronExpression);

        return TriggerBuilder.newTrigger()
                .forJob(licenseRequest113JobDetail())
                .withIdentity("LicenseRequest113")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
