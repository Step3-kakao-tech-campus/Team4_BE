package com.ktc.matgpt;

import com.ktc.matgpt.feature_review.store.Store;
import com.ktc.matgpt.feature_review.store.StoreJPARepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Arrays;

@EnableJpaAuditing
@SpringBootApplication
public class MatgptApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatgptApplication.class, args);
    }

}
