package com.altarit.contrl.web.confuguration;

import com.altarit.contrl.client.ContrlClient;
import com.altarit.contrl.client.api.chat.ChatStatic;
import com.altarit.contrl.client.api.control.ControlStatic;
import com.altarit.contrl.client.api.dispatcher.ActionDispatcher;
import com.altarit.contrl.client.api.other.OtherActions;
import com.altarit.contrl.client.utils.AppEnv;
import com.altarit.contrl.client.workers.WorkPool;
import com.altarit.contrl.web.wservices.WsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan(basePackages = "com.altarit.contrl.client")
public class ContrlConfig {

    private static final Logger log = LoggerFactory.getLogger(ContrlConfig.class);

    @Autowired
    private ContrlClient contrlClient;

    @Autowired
    private ActionDispatcher actionDispatcher;

    @Autowired
    private WsService wsService;

    @Bean
    public WorkPool workPool() {
        return new WorkPool(2);
    }

    @PostConstruct
    public void setupContrl() throws Exception {
        log.debug("setupContrl: web");
        String[] args = (String[]) AppEnv.instance().get("args");
        //log.debug("setupContrl: {}", args);
        contrlClient.start(args);

        actionDispatcher.putAction("ACT_1", (obj)-> {
            log.debug("ACT_1 {}", obj);
        });
        actionDispatcher.putAction(ChatStatic.ChatMessageAction.CHAT__MESSAGE_RESPONSE, (message)-> {
            log.debug("MESSAGE {}", ((ChatStatic.ChatMessageAction)message).getMessage());
            wsService.sendPreparedAction(message);
        });
        actionDispatcher.putAction(ControlStatic.CmdResponse.CONSOLE_SERVER_OUTPUT_RESPONSE, (message)-> {
            log.debug("CONSOLE {}", ((ControlStatic.CmdResponse)message).getText());
            wsService.sendPreparedAction(message);
        });
        actionDispatcher.putAction(OtherActions.UpdatePeersAction.CONNECTIONS_UPDATE_LIST_SUCCESS, (message)-> {
            wsService.sendPreparedAction(message);
        });

        //
        //actionDispatcher.putAction("ACT_2", ChatConsoleHandler::handleChatMessage);
    }
}
