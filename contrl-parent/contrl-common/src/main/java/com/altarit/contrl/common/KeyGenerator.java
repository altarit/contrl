package com.altarit.contrl.common;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyGenerator {

    private static final Logger log = LoggerFactory.getLogger(KeyGenerator.class);

    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public KeyGenerator(int keyLength) throws NoSuchAlgorithmException {
        keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keyLength);
    }

    public void createKeys() {
        pair = keyGen.generateKeyPair();
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdir();

        try (FileOutputStream fos = new FileOutputStream(f)) {
            fos.write(key);
            fos.flush();
            fos.close();
        }
    }

    public void writeToFile(String path, String key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdir();

        try (FileOutputStream fos = new FileOutputStream(f)) {
            fos.write(key.getBytes());
            fos.flush();
            fos.close();
        }
    }

    public static void main(String[] args) {
        KeyGenerator gk;
        try {
            gk = new KeyGenerator(1024);
            gk.createKeys();
            gk.writeToFile("keys/publicKey", Base64.encode(gk.getPublicKey().getEncoded()));
            gk.writeToFile("keys/privateKey", Base64.encode(gk.getPrivateKey().getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }


}
