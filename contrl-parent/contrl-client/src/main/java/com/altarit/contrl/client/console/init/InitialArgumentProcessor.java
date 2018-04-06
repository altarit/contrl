package com.altarit.contrl.client.console.init;

import com.altarit.contrl.client.console.CommandStore;
import com.altarit.contrl.client.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class InitialArgumentProcessor {

    Logger log = LoggerFactory.getLogger(InitialArgumentProcessor.class);

    @Autowired
    private CommandStore commandStore;

    @Autowired
    private InitialConfigProcessor configInitializer;

    @Autowired
    private FileUtils fileUtils;

    private int pos = 0;
    private String[] args;
    private Map<String, Runnable> commands = new HashMap<>();

    public InitialArgumentProcessor() {
    }

    public void init() {
        commands.put("-init", () -> {
            String cshFile = getNextArg();
            if (cshFile == null) {
                log.warn("There is not init file.");
                return;
            }
            ClassLoader classLoader = getClass().getClassLoader();
            //String pathname = new File(classLoader.getResource(cshFile).getPath()).getPath();
            String pathname = new File(cshFile).getPath();
            log.debug("run commands from {}", pathname);
            try (Stream<String> stream = Files.lines(Paths.get(pathname))) {
                //stream.forEach(log::debug);
                stream.forEach(commandStore::execute);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        });
        commands.put("-config", () -> {
            String cprFile = getNextArg();
            if (cprFile == null) {
                log.warn("There is not config file.");
                return;
            }
            ClassLoader classLoader = getClass().getClassLoader();
            //String pathname = new File(classLoader.getResource(cprFile).getPath()).getPath();
            String pathname = new File(cprFile).getPath();
            log.debug("apply config from {}", pathname);
            String json = fileUtils.readAllFromFile(pathname);
            try {
                configInitializer.initByJson(json);
            } catch (Exception e) {
                log.error("Exception at configInitializer.initByJson", e);
                throw new RuntimeException("Exception at configInitializer.initByJson", e);
            }
        });
    }

    public void process(String[] args) {
        this.args = args;
        if (args == null) {
            log.debug("arguments: null");
            return;
        }

        log.debug("arguments: {}", args.length);
        String arg = getNextArg();
        while(arg != null) {
            Runnable runnable = commands.get(arg);
            if (arg != null) {
                runnable.run();
            } else {
                log.debug("Unknown argument: {}", arg);
            }
            arg = getNextArg();
        }
    }

    private String getNextArg() {
        if (pos < args.length) {
            return args[pos++];
        } else {
            return null;
        }
    }
}
