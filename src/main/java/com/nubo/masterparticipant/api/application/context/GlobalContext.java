package com.nubo.masterparticipant.api.application.context;

import java.util.HashMap;
import java.util.Map;

public class GlobalContext {

    private static final ThreadLocal<Map<String, String>> context = ThreadLocal.withInitial(HashMap::new);

    private GlobalContext() {
    }

    public static void setProperty(String key, String value) {
        context.get().put(key, value);
    }

    public static String getProperty(String key) {
        Object value = context.get().get(key);
        return value != null ? String.valueOf(value) : null;
    }

    public static void remove() {
        context.remove();
    }

}
