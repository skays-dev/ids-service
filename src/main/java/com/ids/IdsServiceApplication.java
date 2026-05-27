package com.ids;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableScheduling
@EnableMethodSecurity
@ConfigurationPropertiesScan
public class IdsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdsServiceApplication.class, args);
    }
}
