package cc.winterclient.client.event.ext.scenarios;

import cc.winterclient.client.event.Event;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

public class EventSendPacket extends Event {
    public Packet<? extends INetHandler> packet;

    public EventSendPacket(Packet<? extends INetHandler> packet)
    {
        setPacket(packet);
    }

    public Packet<? extends INetHandler> getPacket()
    {
        return packet;
    }

    public void setPacket(Packet<? extends INetHandler> packet)
    {
        this.packet = packet;
    }
}
