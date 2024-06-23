package com.fpt.fis.template;

import com.fpt.fis.configuration.AuthorizationConfiguration;
import com.fpt.fis.configuration.TenantSettingConfiguration;
import com.fpt.fis.configuration.annotation.EnableNoneRelationDatabaseSecured;
import com.fpt.fis.data.constraint.controller.rest.DataConstraintRestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

@SpringBootApplication
@EnableReactiveMongoAuditing
@EnableNoneRelationDatabaseSecured
@Import({AuthorizationConfiguration.class, TenantSettingConfiguration.class, DataConstraintRestController.class})
public class TemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }

}
