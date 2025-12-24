package com.presight.gateway.filter.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

@Component
@Slf4j
public class RequestResponseLoggingFilter implements GlobalFilter, Ordered {

    private static final String UNKNOWN_VALUE = "unknown";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logRequest(exchange);
        return chain.filter(exchange).doOnSuccess(aVoid -> logResponse(exchange))
                .doOnError(exception -> logResponse(exchange));
    }

    private void logResponse(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        log.info("Processed request: {} is returned with status: {}", getRequestURL(exchange), response.getStatusCode());
    }

    private void logRequest(ServerWebExchange exchange) {
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        String routeId = UNKNOWN_VALUE;
        String routeURI = UNKNOWN_VALUE;
        if(route!= null){
            routeId = route.getId();
            routeURI = route.getUri().toString();
        }
        log.info("Incoming request: {} us routed to route -ID \"{}\", URL: \"{}\"",getRequestURL(exchange), routeId,routeURI);
    }

    private Object getRequestURL(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        return request.getURI().toString();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
