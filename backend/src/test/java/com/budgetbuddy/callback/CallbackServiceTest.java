package com.budgetbuddy.callback;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

class CallbackServiceTest {

    private final Map<String, Map<String, String>> callbacks =  new HashMap<>();
    private final CallbackStorage callbackStorage = new CallbackStorage(callbacks);
    private final CallbackService callbackService = new CallbackService(callbackStorage);

    @ParameterizedTest
    @ValueSource(ints = {0, 2, 5})
    void shouldProcessCallback(int callbackSize) {
        // given
        Map<String, String> expectedCallback = CallbackTestUtil.createDummyCallback(callbackSize);

        // when
        boolean wasProcessed = callbackService.processCallback(expectedCallback);

        // then
        Assertions.assertThat(wasProcessed).isTrue();
    }

    @Test
    void shouldNotProcessCallback() {
        // given
        Map<String, String> expectedCallback = CallbackTestUtil.createDummyCallback(6);

        // when
        boolean wasProcessed = callbackService.processCallback(expectedCallback);

        // then
        Assertions.assertThat(wasProcessed).isFalse();
    }

}