package com.budgetbuddy.callback;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class CallbackStorageTest {

    private final Map<String, Map<String, String>> callbacks =  new HashMap<>();
    private final CallbackStorage callbackStorage = new CallbackStorage(callbacks);

    @Test
    void shouldAddCallbackData() {
        // given
        final CallbackSource callbackSource = CallbackSource.LOCALHOST;
        final Map<String, String> callbackData = CallbackTestUtil.createDummyCallback();

        // when
        callbackStorage.add(callbackSource, callbackData);

        // then
        Assertions.assertThat(callbacks)
                .hasSize(1)
                .containsKey(callbackSource.name() + ":" + CallbackTestUtil.ID_KEY + ":" + CallbackTestUtil.ID_VALUE)
                .containsValue(CallbackTestUtil.createDummyCallback());
    }

    @Test
    void shouldRetrieveAndRemoveCallbackData() {
        // given
        final CallbackSource callbackSource = CallbackSource.LOCALHOST;
        final String fullCallbackKey = callbackSource.name() + ":" + CallbackTestUtil.ID_KEY + ":" + CallbackTestUtil.ID_VALUE;
        final Map<String, String> expectedCallbackData = CallbackTestUtil.createDummyCallback();
        callbacks.put(fullCallbackKey, expectedCallbackData);

        // when
        final Map<String, String> actualCallbackData = callbackStorage.poll(callbackSource, CallbackTestUtil.ID_VALUE);

        // then
        Assertions.assertThat(actualCallbackData).isEqualTo(expectedCallbackData);
        Assertions.assertThat(callbacks).doesNotContainKey(CallbackTestUtil.ID_KEY);
    }

}