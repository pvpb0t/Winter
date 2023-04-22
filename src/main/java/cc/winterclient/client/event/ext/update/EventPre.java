package cc.winterclient.client.event.ext.update;

import cc.winterclient.client.event.Event;

public class EventPre extends Event {
    private double posX, posY, posZ;
    private float yaw, pitch;
    private boolean onGround, isSprinting, isSneaking;

    public EventPre(double posX, double posY, double posZ, float yaw, float pitch, boolean onGround, boolean isSprinting, boolean isSneaking)
    {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.isSprinting = isSprinting;
        this.isSneaking = isSneaking;
    }

    public double getPosX()
    {
        return posX;
    }

    public void setPosX(double posX)
    {
        this.posX = posX;
    }

    public double getPosY()
    {
        return posY;
    }

    public void setPosY(double posY)
    {
        this.posY = posY;
    }

    public double getPosZ()
    {
        return posZ;
    }

    public void setPosZ(double posZ)
    {
        this.posZ = posZ;
    }

    public float getYaw()
    {
        return yaw;
    }

    public void setYaw(float yaw)
    {
        this.yaw = yaw;
    }

    public float getPitch()
    {
        return pitch;
    }

    public void setPitch(float pitch)
    {
        this.pitch = pitch;
    }

    public boolean isOnGround()
    {
        return onGround;
    }

    public void setOnGround(boolean onGround)
    {
        this.onGround = onGround;
    }

    public boolean isSprinting()
    {
        return isSprinting;
    }

    public void setSprinting(boolean sprinting)
    {
        isSprinting = sprinting;
    }

    public boolean isSneaking()
    {
        return isSneaking;
    }

    public void setSneaking(boolean sneaking)
    {
        isSneaking = sneaking;
    }
}
