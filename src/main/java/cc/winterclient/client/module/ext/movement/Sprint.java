package cc.winterclient.client.module.ext.movement;

import cc.winterclient.client.module.Category;
import cc.winterclient.client.event.EventTarget;
import cc.winterclient.client.event.ext.update.EventUpdate;
import cc.winterclient.client.module.Module;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Category.MOVEMENT, 0);
    }

    @EventTarget
    public void onUpdate(EventUpdate event){
        if(mc.player.movementInput.moveForward != 0.0 || mc.player.movementInput.moveStrafe != 0.0){
            mc.player.setSprinting(true);
        }
    }

}
