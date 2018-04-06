package com.altarit.contrl.crypto;

import com.altarit.contrl.crypto.generators.AsymmetricGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MainCrypto {

    private static final Logger log = LoggerFactory.getLogger(MainCrypto.class);

    public static void main(String[] args) {
        log.debug("crypto started");
        ApplicationContext ctx = new AnnotationConfigApplicationContext("com.altarit.contrl");
        MainCrypto main = ctx.getBean(MainCrypto.class);
        main.generateKeys();
    }

    public void generateKeys() {
        AsymmetricGenerator.generateAsymmetric("keys/clientC1");
        AsymmetricGenerator.generateAsymmetric("keys/clientC2");
        AsymmetricGenerator.generateAsymmetric("keys/clientC3");
        AsymmetricGenerator.generateAsymmetric("keys/clientW1");
    }

}
