package com.example.customerservice.config.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;

@Aspect
@RequiredArgsConstructor
public class SpanAspect {

    private final Tracer tracer;


//    @Around("@annotation(WithSpan) && execution(* com.example.customerservice..*(..)))")
    @Around("execution(* com.example.customerservice.con..*(..)))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Span newSpan = this.tracer.nextSpan().name("methodWithSleep");
        try (Tracer.SpanInScope ws = this.tracer.withSpan(newSpan.start())) {
            return joinPoint.proceed();
        } finally {
            newSpan.end();
        }
   }
}
