package com.budgetbuddy.callback;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
enum CallbackSource {
    LOCALHOST("id"),
    TINK("credentialsId");

    private final String callbackKey;

    static final Map<String, CallbackSource> CALLBACK_SOURCES = Arrays.stream(CallbackSource.values())
            .collect(Collectors.toUnmodifiableMap(CallbackSource::getCallbackKey, source -> source));

}
