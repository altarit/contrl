package com.altarit.contrl.crypto.ciphers;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
public class AsymmetricCipher {

    private static final Logger log = LoggerFactory.getLogger(AsymmetricCipher.class);

    private Cipher cipher;
    private SecureRandom random = new SecureRandom();

    public AsymmetricCipher() throws GeneralSecurityException {
        cipher = Cipher.getInstance("RSA");
    }

    public byte[] generateRandom() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return bytes;
    }

    public PublicKey readPublicFromFile(String filename) throws IOException, GeneralSecurityException {
        byte[] keyBytes = Base64.decodeBase64(Files.readAllBytes(new File(filename).toPath()));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    public PublicKey readPublicFromString(String format) throws GeneralSecurityException {
        byte[] keyBytes = Base64.decodeBase64(format);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    public PrivateKey readPrivateFromFile(String filename) throws IOException, GeneralSecurityException {
        byte[] keyBytes = Base64.decodeBase64(Files.readAllBytes(new File(filename).toPath()));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    public PrivateKey readPrivateFromString(String format) throws GeneralSecurityException {
        byte[] keyBytes = Base64.decodeBase64(format);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    public String encryptText(String text, Key key) throws GeneralSecurityException, UnsupportedEncodingException {
        synchronized (cipher) {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.encodeBase64String(cipher.doFinal(text.getBytes("utf-8")));
        }
    }

    public String decryptText(String encryptedText, Key key) throws GeneralSecurityException, UnsupportedEncodingException {
        synchronized (cipher) {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.decodeBase64(encryptedText)), "utf-8");
        }
    }


}
