package cc.winterclient.client.event.ext.update;

import cc.winterclient.client.event.Event;

public class EventPreExtended extends Event {

    public float getLimbswing() {
        return limbswing;
    }

    public void setLimbswing(float limbswing) {
        this.limbswing = limbswing;
    }

    public float getLimbswingAmmount() {
        return limbswingAmmount;
    }

    public void setLimbswingAmmount(float limbswingAmmount) {
        this.limbswingAmmount = limbswingAmmount;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isSprinting() {
        return isSprinting;
    }

    public void setSprinting(boolean sprinting) {
        isSprinting = sprinting;
    }

    public boolean isSneaking() {
        return isSneaking;
    }

    public void setSneaking(boolean sneaking) {
        isSneaking = sneaking;
    }

    public EventPreExtended(float limbswing, float limbswingAmmount, boolean onGround, boolean isSprinting, boolean isSneaking) {
        this.limbswing = limbswing;
        this.limbswingAmmount = limbswingAmmount;
        this.onGround = onGround;
        this.isSprinting = isSprinting;
        this.isSneaking = isSneaking;
    }

    private float limbswing, limbswingAmmount;
    private boolean onGround, isSprinting, isSneaking;

}
