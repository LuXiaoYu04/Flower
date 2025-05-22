package com.flowers.shopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FlowersApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowersApplication.class, args);
    }

}
