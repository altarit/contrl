package com.altarit.contrl.crypto.generators;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class AsymmetricGenerator {

    private static final Logger log = LoggerFactory.getLogger(AsymmetricGenerator.class);

    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public AsymmetricGenerator(int keyLength) throws NoSuchAlgorithmException {
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

    /*public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdir();

        try (FileOutputStream fos = new FileOutputStream(f)) {
            fos.write(key);
            fos.flush();
            fos.close();
        }
    }*/

    public void writeToFile(String path, String key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdir();

        try (FileOutputStream fos = new FileOutputStream(f)) {
            fos.write(key.getBytes());
            fos.flush();
            fos.close();
        }
    }

    public static void generateAsymmetric(String dirPath) {
        AsymmetricGenerator gk;
        try {
            gk = new AsymmetricGenerator(1024);
            gk.createKeys();
            gk.writeToFile(dirPath + "/publicKey.key", Base64.encode(gk.getPublicKey().getEncoded()));
            gk.writeToFile(dirPath + "/privateKey.key", Base64.encode(gk.getPrivateKey().getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
