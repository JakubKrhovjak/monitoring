package com.example.customerservice;

import com.example.customerservice.entity.User;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

import io.micrometer.core.instrument.MeterRegistry;
//import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@SpringBootApplication
@RequiredArgsConstructor
public class CustomerServiceApplication {

    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @GetMapping("/test")
    public ResponseEntity<String> createLogs() {
        log.warn("Just checking");
        return ResponseEntity.ok(methodWithSleep());
    }

    @SneakyThrows

    public String methodWithSleep() {
        Thread.sleep(1000);
        var afterSleep = "After sleep";
        log.info(afterSleep);
        return afterSleep;
    }

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", "customer-service", "instance", "test-instance");
    }


    @GetMapping("/db-test")
    public List<User> test() throws InterruptedException {
        log.info("Db checking");
        userRepository.save(new User().setUsername("test-" + new Random().nextInt(100)).setPassword("pass"));
        return userRepository.findAll();
    }

}
