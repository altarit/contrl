package com.altarit.contrl.client.tasks;

import com.altarit.contrl.client.api.control.ControlRemoteApi;
import com.altarit.contrl.client.network.NetConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
@Scope("prototype")
public class ShellRunner implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ShellRunner.class);

    @Autowired
    private ControlRemoteApi controlRemoteApi;

    private String command;
    private NetConnection connection;

    public ShellRunner() {
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setConnection(NetConnection connection) {
        this.connection = connection;
    }

    public void run() {
        try {
            log.debug("[{}] started", Thread.currentThread().getId());
            executeCommand();
            log.debug("[{}] finished", Thread.currentThread().getId());
        } catch (Exception e) {
            log.error("Exception at system terminal", e);
        }
    }

    private void executeCommand() throws IOException {
        log.debug("exec start: {}", command);
        ProcessBuilder pb = new ProcessBuilder("cmd.exe");
        pb.command(command.split(" +"));
        pb.redirectErrorStream(true);
        Process process = pb.start();
        BufferedReader inStreamReader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), "IBM866"));

        String line;
        while (true) {
            line = inStreamReader.readLine();
            if (line == null) {
                break;
            }
            controlRemoteApi.sendResponseFromCmd(connection, line);
        }
    }

//    private void executeCommandOld() {
//
//        log.debug("exec start: {}", command);
//
//        StringBuffer output = new StringBuffer();
//
//        Process p;
//        try {
//            p = Runtime.getRuntime().exec(command);
//            p.waitFor();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//
//            String line = "";
//            while ((line = reader.readLine()) != null) {
//                output.append(line + "\n");
//                log.debug("-" + line);
//                chatRemoteApi.sendMessage(connection, "+" + line);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        log.debug("exec complete");
//    }
}
