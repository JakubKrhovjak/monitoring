package com.example.customerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

//    @Bean
//    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
//        return registry -> registry.config().commonTags("application", "customer-service", "instance", "test-instance");
//    }



}
