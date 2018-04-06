package com.altarit.contrl.crypto.store;

import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;

@Component
public class SecretStore {

    private PublicKey publicEncryptKey;

    private PrivateKey privateDecryptKey;

    public PublicKey getPublicEncryptKey() {
        return publicEncryptKey;
    }

    public void setPublicEncryptKey(PublicKey publicEncryptKey) {
        this.publicEncryptKey = publicEncryptKey;
    }

    public PrivateKey getPrivateDecryptKey() {
        return privateDecryptKey;
    }

    public void setPrivateDecryptKey(PrivateKey privateDecryptKey) {
        this.privateDecryptKey = privateDecryptKey;
    }
}
