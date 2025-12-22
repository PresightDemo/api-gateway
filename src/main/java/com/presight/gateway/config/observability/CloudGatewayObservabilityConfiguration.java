package com.presight.gateway.config.observability;

import com.presight.common_lib.observability.trace.MicrometerBasedTraceIdProvider;
import com.presight.common_lib.observability.trace.TraceIdProvider;
import com.presight.gateway.filter.traceidheader.AddTraceIdResponseHeaderFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Slf4j
@Import(MicrometerBasedTraceIdProvider.class)
public class CloudGatewayObservabilityConfiguration {

    @Bean
    public AddTraceIdResponseHeaderFilter traceIdResponseHeaderFilter (TraceIdProvider traceIdProvider){
        log.info("Creating filter to add trace Id to response");
        return  new AddTraceIdResponseHeaderFilter(traceIdProvider);
    }
}
//