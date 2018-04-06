package com.altarit.contrl.console.configuration;

import com.altarit.contrl.client.api.chat.ChatStatic;
import com.altarit.contrl.client.api.dispatcher.AbstractAction;
import com.altarit.contrl.client.api.dispatcher.ActionDispatcher;
import com.altarit.contrl.client.workers.WorkPool;
import com.altarit.contrl.console.api.ChatConsoleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.function.Function;

@Configuration
@ComponentScan(basePackages = "com.altarit.contrl.client")
public class ConsoleContrlConfig {

    private static final Logger log = LoggerFactory.getLogger(ConsoleContrlConfig.class);

    @Autowired
    private ActionDispatcher actionDispatcher;

    @Bean
    public WorkPool workPool() {
        return new WorkPool(4);
    }

    @PostConstruct
    public void setupContrl() throws Exception {
        log.debug("setupContrl: console");
        actionDispatcher.putAction("ACT_1", (obj)-> {
            log.debug("ACT_1 {}", obj);
        });
        actionDispatcher.putAction(ChatStatic.ChatMessageAction.CHAT__MESSAGE_RESPONSE, (message)-> {
            log.debug("MESSAGE {}", ((ChatStatic.ChatMessageAction)message).getMessage());
        });
        //
        //actionDispatcher.putAction("ACT_2", ChatConsoleHandler::handleChatMessage);
    }
}
