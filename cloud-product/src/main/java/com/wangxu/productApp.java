package com.wangxu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class productApp {
    public static void main(String[] args) {
        SpringApplication.run(productApp.class,args);
    }
}
