package com.budgetbuddy.callback;

import java.util.HashMap;
import java.util.Map;

class CallbackTestUtil {

    static final String ID_KEY = "id";
    static final String ID_VALUE = "id-value";
    static final String CODE_KEY = "code";

    static Map<String, String> createDummyCallback() {
        return Map.of(ID_KEY, "id-value", CODE_KEY, "code-value");
    }

    static Map<String, String> createDummyCallback(int size) {
        final Map<String, String> dummyCallback = new HashMap<>();
        for (int i = 0; i < size; i++) {
            dummyCallback.put(i == 0 ? ID_KEY : ID_KEY + i, ID_VALUE);
        }

        return dummyCallback;
    }
}
