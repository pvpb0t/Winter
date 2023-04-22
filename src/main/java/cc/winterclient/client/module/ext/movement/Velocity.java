package cc.winterclient.client.module.ext.movement;

import cc.winterclient.client.module.Category;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import cc.winterclient.client.event.EventTarget;
import cc.winterclient.client.event.ext.scenarios.EventReceivePacket;
import cc.winterclient.client.module.Module;

public class Velocity extends Module {


    public Velocity() {
        super("Velocity", Category.MOVEMENT, 0);
    }

    @EventTarget
    public void onReceive(EventReceivePacket event){
        if(event.getPacket() instanceof SPacketEntityVelocity){
            final SPacketEntityVelocity sPacketEntityVelocity = (SPacketEntityVelocity) event.getPacket();
            if (sPacketEntityVelocity.getEntityID() == mc.player.entityId) {
                sPacketEntityVelocity.motionX = 0;
                sPacketEntityVelocity.motionY = 0;
                sPacketEntityVelocity.motionZ = 0;
            }
        }
    }

}
