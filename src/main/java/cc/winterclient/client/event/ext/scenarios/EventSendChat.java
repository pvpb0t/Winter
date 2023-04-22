package cc.winterclient.client.event.ext.scenarios;

import cc.winterclient.client.event.Event;

public class EventSendChat extends Event {

    private String message;

    public EventSendChat(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
