package com.budgetbuddy.callback;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
class CallbackService {

    private static final int MAX_CALLBACK_SIZE = 5;

    private final CallbackStorage callbackStorage;

    boolean processCallback(Map<String, String> callbackData) {
        final int callbackSize = callbackData.size();
        log.info("Received callback data of size: {}", callbackSize);
        if (callbackSize == 0) {
            return true;
        }

        if (callbackSize > MAX_CALLBACK_SIZE) {
            log.warn("Callback data is exceeding max allowed size");
            return false;
        }

        return storeCallback(callbackData);
    }

    private boolean storeCallback(Map<String, String> callbackData) {
        final CallbackSource callbackSource = getCallbackSource(callbackData);
        if (callbackSource == null) {
            log.warn("Key not defined, callback will not be stored");
            return false;
        }
        callbackStorage.add(callbackSource, callbackData);
        return true;
    }

    // TODO: Think if callback source is even needed, if in the end yes, prepare better solution
    // This can be a bit buggy, not big fan of current design
    private CallbackSource getCallbackSource(Map<String, String> callbackData) {
        Optional<String> callbackKey = Sets.intersection(CallbackSource.CALLBACK_SOURCES.keySet(), callbackData.keySet()).stream().findAny();
        return CallbackSource.CALLBACK_SOURCES.get(callbackKey.orElse(null));
    }

}
