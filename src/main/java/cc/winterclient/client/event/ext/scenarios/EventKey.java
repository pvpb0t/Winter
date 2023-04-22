package cc.winterclient.client.event.ext.scenarios;

import cc.winterclient.client.event.Event;

public class EventKey extends Event {

    private int key;

    public EventKey(int key){
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
