package com.altarit.contrl.client.api.control;

import com.altarit.contrl.client.api.chat.ChatRemoteApi;
import com.altarit.contrl.client.api.dispatcher.ActionDispatcher;
import com.altarit.contrl.client.network.LocalApi;
import com.altarit.contrl.client.network.NetConnection;
import com.altarit.contrl.client.tasks.ShellRunner;
import com.altarit.contrl.client.tasks.Timer;
import com.altarit.contrl.client.utils.SpringUtils;
import com.altarit.contrl.client.workers.WorkPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.altarit.contrl.client.api.control.ControlStatic.*;

@Component
public class ControlLocalApi implements LocalApi {

    private static final Logger log = LoggerFactory.getLogger(ControlLocalApi.class);

    @Autowired
    protected WorkPool workPool;

    @Autowired
    protected ChatRemoteApi chatRemoteApi;

    @Autowired
    private ActionDispatcher actionDispatcher;

    public void receive(NetConnection con, Object object) {
        if (object instanceof ControlStatic.SayWait) {
            sayWait((SayWait) object);
            return;
        }
        if (object instanceof CmdRequest) {
            shellCmd(con, (CmdRequest)object);
            return;
        }
        if (object instanceof ControlStatic.CmdResponse) {
            receiveCmdResponse(con, (CmdResponse)object);
            return;
        }
    }

    private void sayWait(SayWait message) {
        workPool.addTask(new Timer(message.millis, "hi"));
    }

    private void shellCmd(NetConnection con, CmdRequest message) {
        ShellRunner shellRunner = SpringUtils.getBean(ShellRunner.class);
        shellRunner.setCommand(message.command);
        shellRunner.setConnection(con);
        workPool.addTask(shellRunner);
        //String result = executeCommand(connection, shellCmd.command);
        //chatRemoteApi.sendMessage(connection, result);
    }

    private void receiveCmdResponse(NetConnection con, CmdResponse message) {
        actionDispatcher.dispatch(message);
    }




}
