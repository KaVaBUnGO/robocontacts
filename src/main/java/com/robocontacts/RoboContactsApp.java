package com.robocontacts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RoboContactsApp {
    public static void main(String[] args) {
        SpringApplication.run(RoboContactsApp.class, args);
    }
}
