package com.xmcy.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.xmcy")
public class Application {

    static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
