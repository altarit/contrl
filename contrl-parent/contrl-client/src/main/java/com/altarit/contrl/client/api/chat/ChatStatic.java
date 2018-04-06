package com.altarit.contrl.client.api.chat;

import com.altarit.contrl.client.api.dispatcher.AbstractAction;

public class ChatStatic {

    //public static final String CHAT__SEND_MESSAGE = "CHAT__SEND_MESSAGE";

    static public class ChatMessageAction extends AbstractAction {
        public static final String CHAT__MESSAGE_RESPONSE = "CHAT__MESSAGE_RESPONSE";

        public String message;

        public ChatMessageAction() {
            super(CHAT__MESSAGE_RESPONSE);
        }

        public ChatMessageAction(String message) {
            this();
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
