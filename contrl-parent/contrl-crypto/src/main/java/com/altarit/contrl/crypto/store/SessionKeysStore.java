package com.altarit.contrl.crypto.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionKeysStore {

    private static final Logger log = LoggerFactory.getLogger(SessionKeysStore.class);

    private Map<String, SessionKeyData> map = new ConcurrentHashMap<>();

    public void putSessionData(String peerId, SecretKey secretKey) {
        map.put(peerId, new SessionKeyData(secretKey));
    }

    public SecretKey getSecretKey(String peerId) {
        return map.get(peerId).secretKey;
    }

    static class SessionKeyData {
        public SessionKeyData(SecretKey secretKey) {
            this.secretKey = secretKey;
        }

        public SecretKey secretKey;
    }
}
