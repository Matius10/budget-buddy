package com.budgetbuddy.callback;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Callback Storage lives only as long as app, there is no need of keeping this data longer.
 */
@Component
@AllArgsConstructor
public class CallbackStorage {

    private final Map<String, Map<String, String>> callbacks;

    /**
     * Removing callback data after retrieving it as this data should be for single use only.
     */
    public Map<String, String> poll(@NonNull CallbackSource callbackSource, @NonNull String key) {
        String callbackKey = createCallbackKey(callbackSource, key);
        final Map<String, String> callbackData = callbacks.get(callbackKey);
        callbacks.remove(callbackKey);
        return callbackData;
    }

    void add(@NonNull CallbackSource callbackSource, @NonNull Map<String, String> callbackData) {
        callbacks.put(createCallbackKey(callbackSource, callbackData), callbackData);
    }

    private static String createCallbackKey(CallbackSource callbackSource, Map<String, String> callbackData) {
        String callbackValue = callbackData.get(callbackSource.getCallbackKey());
        return formatCallbackKey(callbackSource, callbackValue);
    }

    private static String createCallbackKey(CallbackSource callbackSource, String key) {
        return formatCallbackKey(callbackSource, key);
    }

    private static String formatCallbackKey(CallbackSource callbackSource, String value) {
        return new StringBuilder()
                .append(callbackSource.name())
                .append(":")
                .append(callbackSource.getCallbackKey())
                .append(":")
                .append(value)
                .toString();
    }

}
