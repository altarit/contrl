package com.altarit.contrl.crypto.ciphers;

import org.springframework.stereotype.Component;

import javax.crypto.KeyAgreement;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class KeyAgreementCipher {
    public static void main(String[] args) throws Exception {
        KeyAgreementCipher keyAgreementCipher = new KeyAgreementCipher();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        KeyPair aKeyPar = keyAgreementCipher.generateDHKeys();
        byte[] aPublicKeyBin = aKeyPar.getPublic().getEncoded();
        System.out.println("Public Key [" + aPublicKeyBin.length + "]: " + printHexBinary(aPublicKeyBin));

        System.out.print("input key: ");
        byte[] bPublicKeyBin = parseHexBinary(reader.readLine());
        PublicKey bPublicKey = keyAgreementCipher.getPublicKey(bPublicKeyBin);

        byte[] preSecret = keyAgreementCipher.generatePreMaster(aKeyPar.getPrivate(), bPublicKey);
        System.out.println("Shared secret [" + preSecret.length + "]: " + printHexBinary(preSecret));

        byte[] masterKey = keyAgreementCipher.generateMasterSecret(preSecret, aPublicKeyBin, bPublicKeyBin);
        System.out.println("Final key [" + masterKey.length + "]: " + printHexBinary(masterKey));
    }

    public static String printHexBinary(byte[] bytes) {
        return Arrays.toString(bytes);
    }

    public static byte[] parseHexBinary(String str) {
        String[] strNumbers = str.split("[ ,]+");
        byte[] result = new byte[strNumbers.length];
        for(int i=0, j=0; i<strNumbers.length; i++) {
            try {
                result[j++] = Byte.parseByte(strNumbers[i]);
            } catch (NumberFormatException e) {
                System.out.println("skip '" + strNumbers[i] + "'");
            }
        }
        return result;
    }

    public KeyPair generateDHKeys() {
        try {
            // Generate ephemeral ECDH keypair
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            keyPairGenerator.initialize(256);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public PublicKey getPublicKey(byte[] bPublicKeyBin) {
        try {
            KeyFactory kf = KeyFactory.getInstance("EC");
            X509EncodedKeySpec pkSpec = new X509EncodedKeySpec(bPublicKeyBin);
            return kf.generatePublic(pkSpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] generatePreMaster(PrivateKey privateKey, PublicKey publicKey) {
        try {
            // Perform key agreement
            KeyAgreement ka = KeyAgreement.getInstance("ECDH");
            ka.init(privateKey);
            ka.doPhase(publicKey, true);
            return ka.generateSecret();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] generateMasterSecret(byte[] preSecret, byte[] aPublicKey, byte[] bPublicKey) {
        try {
            // Derive a key from the shared secret and both public keys
            MessageDigest hash = MessageDigest.getInstance("SHA-256");
            hash.update(preSecret);
            // Simple deterministic ordering
            List<ByteBuffer> keys = Arrays.asList(ByteBuffer.wrap(aPublicKey), ByteBuffer.wrap(bPublicKey));
            Collections.sort(keys);
            hash.update(keys.get(0));
            hash.update(keys.get(1));
            return hash.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }




}
