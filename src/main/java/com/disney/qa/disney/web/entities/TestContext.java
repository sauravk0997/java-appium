package com.disney.qa.disney.web.entities;

import java.util.LinkedHashMap;

public class TestContext {
    private TestContext(){}
    private static ThreadLocal<LinkedHashMap<String, Object>> map = ThreadLocal.withInitial(LinkedHashMap::new);

    public static Object getvalue(String key) {
        return ((LinkedHashMap)map.get()).get(key);
    }

    public static void putvalue(String key, Object value) {
        ((LinkedHashMap)map.get()).put(key, value);
    }

    public static void removeValue(String key) {
        ((LinkedHashMap)map.get()).remove(key);
    }
}
