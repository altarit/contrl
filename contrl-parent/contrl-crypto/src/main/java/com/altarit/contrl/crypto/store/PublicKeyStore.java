package com.altarit.contrl.crypto.store;

import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PublicKeyStore {

    private Map<String, PublicKey> publicKeys = new ConcurrentHashMap<>();

    public void put(String id, PublicKey key) {
        publicKeys.put(id, key);
    }

    public PublicKey get(String id) {
        return publicKeys.get(id);
    }
}
