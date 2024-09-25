package com.fpt.fis.license_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

@SpringBootApplication
@EnableReactiveMongoAuditing
public class LicenseManagementService {

    public static void main(String[] args) {
        SpringApplication.run(LicenseManagementService.class, args);
    }

}
