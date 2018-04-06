package com.altarit.contrl.crypto.ciphers;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;

@Component
public class SymmetricCipher {

    private static final Logger log = LoggerFactory.getLogger(SymmetricCipher.class);

    public SecretKeySpec generateKey(String format) {
        SecretKeySpec secretKeySpec;
        try {
            secretKeySpec = new SecretKeySpec(format.getBytes("utf-8"), "AES");
            return secretKeySpec;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String encrypt(String text, Key key) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.encodeBase64String(cipher.doFinal(text.getBytes("utf-8")));
    }

    public byte[] encrypt(String text, SecretKey secretKeySpec, byte[] initVector) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(initVector);
            //log.debug("key({})={}", key.length, Arrays.toString(key));
            //SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
            return cipher.doFinal(text.getBytes());
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String cipherText, Key key) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.decodeBase64(cipherText)), "utf-8");
    }

    public String decrypt(byte[] cipherText, SecretKey secretKeySpec, byte[] initVector) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(initVector);
            //SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
            byte[] decrypted = cipher.doFinal(cipherText);
            return new String(decrypted);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
