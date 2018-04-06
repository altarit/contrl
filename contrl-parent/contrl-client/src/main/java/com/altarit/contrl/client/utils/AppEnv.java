package com.altarit.contrl.client.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppEnv {

    private static volatile AppEnv instance = new AppEnv();

    private Map<String, Object> map = new ConcurrentHashMap<>();

    private AppEnv() {
    }

    public static AppEnv instance() {
        return instance;
    }

    public void put(String s, Object o) {
        map.put(s, o);
    }

    public Object get(String s) {
        return map.get(s);
    }

    public Object remove(String s) {
        return map.remove(s);
    }

}
