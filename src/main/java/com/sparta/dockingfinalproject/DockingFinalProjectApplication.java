package com.sparta.dockingfinalproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DockingFinalProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockingFinalProjectApplication.class, args);
    }

}
