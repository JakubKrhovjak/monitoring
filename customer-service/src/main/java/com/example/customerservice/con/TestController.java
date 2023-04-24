package com.example.customerservice.con;


import com.example.customerservice.UserRepository;
import com.example.customerservice.config.aspect.WithSpan;
import com.example.customerservice.entity.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    private final UserRepository userRepository;

    @GetMapping("/test")
    public ResponseEntity<String> createLogs () {
        log.warn("Just checking");
        return ResponseEntity.ok(methodWithSleep());
    }


    @SneakyThrows
    @WithSpan
    public String methodWithSleep() {
        Thread.sleep(1000);
        var afterSleep = "After sleep";
        log.info(afterSleep);
        //        span.end();
        return afterSleep;
        //        Span newSpan = this.tracer.nextSpan().name("methodWithSleep");
        //
        //        try (Tracer.SpanInScope ws = this.tracer.withSpan(newSpan.start())) {
        //            //        var span = tracer.spanBuilder("test name1").startSpan();
        //
        //            Thread.sleep(1000);
        //            var afterSleep = "After sleep";
        //            log.info(afterSleep);
        //            //        span.end();
        //            return afterSleep;
        //        } finally {
        //            newSpan.end();
        //        }

    }

    @GetMapping("/db-test")
    public List<User> test() throws InterruptedException {
        log.info("Db checking");
        userRepository.save(new User().setUsername("test-" + new Random().nextInt(100)).setPassword("pass"));
        return userRepository.findAll();
    }
}
