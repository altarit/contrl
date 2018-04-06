package com.altarit.contrl.client.api.dispatcher;

import com.altarit.contrl.client.network.LocalApi;
import com.altarit.contrl.client.network.NetConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageDispatcherLocalApi {

    private static final Logger log = LoggerFactory.getLogger(MessageDispatcherLocalApi.class);

    private Map<Class, LocalApi> localApis = new ConcurrentHashMap<>();

    public void receive(NetConnection con, Object object, Class clazz) {
        LocalApi api = localApis.get(clazz);
        if (api == null) {
            log.error("Class {} is not registered", clazz);
            return;
        }
        try {
            api.receive(con, object);
        } catch (Exception e) {
            log.error("Exception at processing incoming message " + e.getMessage(), e);
        }
    }

    public void register(Class clazz, LocalApi localApi) throws IllegalStateException {
        if (localApis.get(clazz) != null) {
            throw new IllegalStateException("Class " + clazz + " is already registered");
        }
        localApis.put(clazz, localApi);
    }
}
