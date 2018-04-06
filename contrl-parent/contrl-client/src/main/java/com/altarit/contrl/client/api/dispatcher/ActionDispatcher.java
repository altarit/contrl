package com.altarit.contrl.client.api.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Component
public class ActionDispatcher {

    private static final Logger log = LoggerFactory.getLogger(ActionDispatcher.class);

    private Map<String, Consumer<AbstractAction>> actionMapping = new ConcurrentHashMap<>();

    public void putAction(String name, Consumer<AbstractAction> func) {
        actionMapping.put(name, func);
    }

    public void dispatch(AbstractAction action) {
        Consumer<AbstractAction> func = actionMapping.get(action.getType());
        if (func != null) {
            func.accept(action);
        } else {
            log.debug("Action {} does not have handler", action.getType());
        }
    }

}
