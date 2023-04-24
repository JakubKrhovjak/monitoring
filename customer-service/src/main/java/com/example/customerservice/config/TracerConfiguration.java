package com.example.customerservice.config;

import com.example.customerservice.config.aspect.SpanAspect;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import brave.Tracing;
import brave.baggage.BaggageField;
import brave.baggage.BaggagePropagation;
import brave.baggage.BaggagePropagationConfig;
import brave.context.slf4j.MDCScopeDecorator;
import brave.propagation.B3Propagation;
import brave.propagation.ThreadLocalCurrentTraceContext;
import brave.sampler.Sampler;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveBaggageManager;
import io.micrometer.tracing.brave.bridge.BraveCurrentTraceContext;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.brave.ZipkinSpanHandler;
import zipkin2.reporter.urlconnection.URLConnectionSender;

@Configuration
public class TracerConfiguration {

    @Value("${spring.application.name}")
    private String applicationName;

    ThreadLocalCurrentTraceContext braveCurrentTraceContext = ThreadLocalCurrentTraceContext.newBuilder()
                                                                                            .addScopeDecorator(
                                                                                                    MDCScopeDecorator.get())
                                                                                            .build();
    @Bean
    public Tracer tracer() {
        return new BraveTracer(tracing().tracer(),
                               new BraveCurrentTraceContext(this.braveCurrentTraceContext),
                               new BraveBaggageManager());
    }

    private Tracing tracing() {
        var spanHandler = ZipkinSpanHandler
                .create(AsyncReporter.create(URLConnectionSender.create("http://localhost:9411/api/v2/spans")));
        return Tracing.newBuilder()
                      .currentTraceContext(this.braveCurrentTraceContext)
                      .localServiceName(applicationName)
                      .supportsJoin(false)
                      .traceId128Bit(true)
                      // For Baggage to work you need to provide a list of fields to propagate
                      .propagationFactory(BaggagePropagation.newFactoryBuilder(B3Propagation.FACTORY)
                                                            .add(BaggagePropagationConfig.SingleBaggageField.remote(
                                                                    BaggageField.create("from_span_in_scope 1")))
                                                            .add(BaggagePropagationConfig.SingleBaggageField.remote(
                                                                    BaggageField.create("from_span_in_scope 2")))
                                                            .add(BaggagePropagationConfig.SingleBaggageField.remote(
                                                                    BaggageField.create("from_span")))
                                                            .build())
                      .sampler(Sampler.ALWAYS_SAMPLE)
                      .addSpanHandler(spanHandler)
                      .build();
    }

    @Bean
    public SpanAspect spanAspect(Tracer tracer) {
        return new SpanAspect(tracer);
    }
}
