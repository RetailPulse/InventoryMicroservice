package com.retailpulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableCaching
@EnableFeignClients(basePackages = "com.retailpulse.client")
public class InventoryMicroservice {
    public static void main(String[] args) {
        SpringApplication.run(InventoryMicroservice.class, args);
    }
}