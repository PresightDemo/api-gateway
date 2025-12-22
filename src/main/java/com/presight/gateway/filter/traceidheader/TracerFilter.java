package com.presight.gateway.filter.traceidheader;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TracerFilter implements GatewayFilter {

    private final Tracer tracer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Manually attach the trace context to the request headers
        exchange.getRequest().mutate()
                .header("X-PRESIGHT-TraceId", Objects.requireNonNull(tracer.currentSpan()).context().traceId())
                .header("X-PRESIGHT-SpanId", Objects.requireNonNull(tracer.currentSpan()).context().spanId())
                .build();

        return chain.filter(exchange);
    }
}
