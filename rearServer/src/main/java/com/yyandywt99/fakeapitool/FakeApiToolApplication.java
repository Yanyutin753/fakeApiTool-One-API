package com.yyandywt99.fakeapitool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author YANGYANG
 */ //定时注解
@EnableScheduling
@SpringBootApplication
public class FakeApiToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(FakeApiToolApplication.class, args);
    }

}
