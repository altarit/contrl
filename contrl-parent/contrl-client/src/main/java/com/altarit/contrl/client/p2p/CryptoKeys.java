package com.altarit.contrl.client.p2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;
import java.security.PublicKey;

public class CryptoKeys {

    private static final Logger log = LoggerFactory.getLogger(CryptoKeys.class);

    private KeyPair thisDHEPair;
    private PublicKey otherDHEPublicKey;
    private byte[] symmetricKey;



}
