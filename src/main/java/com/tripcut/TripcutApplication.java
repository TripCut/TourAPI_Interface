package com.tripcut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.tripcut")
public class TripcutApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripcutApplication.class, args);
    }

}
