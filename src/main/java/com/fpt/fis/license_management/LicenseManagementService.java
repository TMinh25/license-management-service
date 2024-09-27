package com.fpt.fis.license_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableReactiveMongoAuditing
@EnableScheduling
public class LicenseManagementService {

    public static void main(String[] args) {
        SpringApplication.run(LicenseManagementService.class, args);
    }

}
