package com.budgetbuddy.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WebConfiguration implements WebMvcConfigurer {

    private final String callbackPort;
    private final String callbackPath;
    private final String serverContextPath;
    private final ObjectMapper objectMapper;

    WebConfiguration(@Value("${server.servlet.context-path}") String serverContextPath,
                     @Value("${server.callback.port}") String callbackPort,
                     @Value("${server.callback.path}") String callbackPath,
                     @NonNull ObjectMapper objectMapper) {
        this.serverContextPath = serverContextPath;
        this.callbackPort = callbackPort;
        this.callbackPath = callbackPath;
        this.objectMapper = objectMapper;
    }

    @Bean
    FilterRegistrationBean<PortFilter> portPathAccessFilter() {
        return new FilterRegistrationBean<>(new PortFilter(callbackPort, serverContextPath + callbackPath, objectMapper));
    }

}
