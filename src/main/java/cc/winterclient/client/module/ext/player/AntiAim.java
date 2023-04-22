package cc.winterclient.client.module.ext.player;

import cc.winterclient.client.event.EventTarget;
import cc.winterclient.client.event.ext.update.EventPre;
import cc.winterclient.client.module.Category;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.option.Option;
import cc.winterclient.client.option.ext.EnumOption;
import cc.winterclient.client.util.logger.Logger;

import java.util.Arrays;

public class AntiAim extends Module {

    private Option<String> pitch = new EnumOption("Pitch", "None", Arrays.asList("None", "Emotion", "Up"), this);
    private Option<String> yaw = new EnumOption("Yaw", "None", Arrays.asList("None", "Backwards", "Sideways Jitter"), this);

    public AntiAim() {
        super("AntiAim", "Breaking Minecraft resolvers since 2022", Category.MISC, 0, false);
    }
    boolean s = false;
    @EventTarget
    public void pre(EventPre event){
        float p = mc.player.rotationPitch;
        float y = mc.player.rotationYaw;

        mc.player.limbSwingAmount=10;

        switch (pitch.getExact()){
            case "None":
                break;
            case "Emotion":
                p = 89;
                break;
            case "Up":
                p = -89;
                break;
        }

        switch (yaw.getExact()){
            case "None":
                break;
            case "Backwards":
                y += 180;
                break;
            case "Sideways Jitter":
                y += s ? 90 : -90;
                break;
        }

        event.setPitch(p);
        event.setYaw(y);

        s = !s;
    }

}
