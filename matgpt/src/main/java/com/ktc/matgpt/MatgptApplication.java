package com.ktc.matgpt;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class MatgptApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatgptApplication.class, args);
    }


}
