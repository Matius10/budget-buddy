package com.budgetbuddy.callback;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
class CallbackConfiguration {

    @Bean
    CallbackStorage createCallbackStorage() {
        return new CallbackStorage(new ConcurrentHashMap<>());
    }

}
