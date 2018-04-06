package com.altarit.contrl.crypto.ciphers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.spec.KeySpec;

@Component
public class HashCipher {

    private static final Logger log = LoggerFactory.getLogger(HashCipher.class);

    public SecretKey getSecretKey(byte[] original, byte[] salt) {
        try {
            /*SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            char[] charBuffer = new String(original).toCharArray();
            log.debug("original({}) -> char({})", original.length, charBuffer.length);
            KeySpec spec = new PBEKeySpec(charBuffer, salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
            return secret;   */
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] secretBytes = md.digest(original);
            //TODO: don't forget to return 256 bit keys
            byte[] secretBytes16 = new byte[16];
            System.arraycopy(secretBytes, 0, secretBytes16, 0, 16);
            SecretKey secret = new SecretKeySpec(secretBytes16, "AES");
            return secret;

        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
