package cc.winterclient.client.event.ext.scenarios;

import cc.winterclient.client.event.Event;
import net.minecraft.entity.Entity;

public class EventPush extends Event {
    public Entity entity;
    public double x;
    public double y;
    public double z;
    public boolean airbone;

    public EventPush(Entity entity, double d, double d2, double d3, boolean bl) {
        this.entity = entity;
        this.x = d;
        this.y = d2;
        this.z = d3;
        this.airbone = bl;
    }

    public EventPush(){

    }

    public EventPush(Entity entity) {
        this.entity = entity;
    }
}
