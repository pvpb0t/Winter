package cc.winterclient.client.event.ext.scenarios;

import cc.winterclient.client.event.Event;

public class EventArrowLoose extends Event {

    private float charge;

    public float getCharge() {
        return charge;
    }

    public void setCharge(float charge) {
        this.charge = charge;
    }

    public EventArrowLoose(float charge) {
        this.charge = charge;
    }
}
