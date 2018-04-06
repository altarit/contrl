package com.altarit.contrl.client.api.control;

import com.altarit.contrl.client.api.dispatcher.AbstractAction;

public class ControlStatic {
    static public class SayWait extends AbstractAction {
        public static final String REMOTE_COMMAND_WAIT_REQUEST = "REMOTE_COMMAND_WAIT_REQUEST";

        public long millis;

        public SayWait() {
            super(REMOTE_COMMAND_WAIT_REQUEST);
        }

        public SayWait(long millis) {
            this();
            this.millis = millis;
        }

        public long getMillis() {
            return millis;
        }

        public void setMillis(long millis) {
            this.millis = millis;
        }
    }

    static public class CmdRequest extends AbstractAction {
        public static final String CONSOLE_SERVER_COMMAND_REQUEST = "CONSOLE_SERVER_COMMAND_REQUEST";

        public String command;

        public CmdRequest() {
            super(CONSOLE_SERVER_COMMAND_REQUEST);
            System.out.println("new CmdRequest()");
        }

        public CmdRequest(String command) {
            this();
            System.out.println("new CmdRequest(String)");
            this.command = command;
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }
    }

    static public class CmdResponse extends AbstractAction {
        public static final String CONSOLE_SERVER_OUTPUT_RESPONSE = "CONSOLE_SERVER_OUTPUT_RESPONSE";

        public String text;

        public CmdResponse() {
            super(CONSOLE_SERVER_OUTPUT_RESPONSE);
        }

        public CmdResponse(String text) {
            this();
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
