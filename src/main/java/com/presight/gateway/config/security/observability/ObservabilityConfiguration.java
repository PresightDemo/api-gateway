package com.presight.gateway.config.security.observability;

import com.presight.common_lib.observability.trace.TraceConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TraceConfiguration.class)
public class ObservabilityConfiguration {
}