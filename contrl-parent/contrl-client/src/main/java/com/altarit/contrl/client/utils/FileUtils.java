package com.altarit.contrl.client.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    public String readAllFromFile(String pathname) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(pathname));
            return new String(encoded, "utf-8");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new UncheckedIOException(e);
        }
    }
}
