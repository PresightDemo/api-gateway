package com.presight.gateway.filter.traceidheader;

import com.presight.common_lib.observability.trace.MicrometerBasedTraceIdProvider;
import com.presight.common_lib.observability.trace.TraceIdProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Slf4j
public class AddTraceIdResponseHeaderFilter implements GlobalFilter, Ordered {
    static final String TRACE_ID_RESPONSE_HEADER = "PRESIGHT-TRACE-ID";
    private final TraceIdProvider traceIdProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("trace ID : {}", traceIdProvider.getTraceID());
        traceIdProvider.getTraceID().ifPresent(
                traceID -> exchange.getResponse().getHeaders()
                        .add(TRACE_ID_RESPONSE_HEADER, traceID)
        );
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
