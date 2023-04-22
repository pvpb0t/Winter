package cc.winterclient.client;

import cc.winterclient.client.event.EventTarget;
import cc.winterclient.client.event.Priority;
import cc.winterclient.client.event.ext.render.EventModelRotation;
import cc.winterclient.client.event.ext.update.EventPre;
import cc.winterclient.client.event.ext.update.EventTick;
import cc.winterclient.client.util.math.MathUtil;
import net.minecraft.client.Minecraft;

public class RotationAnimator {

    float yaw, pitch, prevYaw, prevPitch;



    @EventTarget(value = Priority.FIFTH)
    public void onPre(EventPre e){
        yaw = e.getYaw();
        pitch = e.getPitch();
    }

    @EventTarget
    public void onTick(EventTick e){
        prevYaw = yaw;
        prevPitch = pitch;
    }

    @EventTarget
    public void onPostAnimation(EventModelRotation e){
        if (e.getEntity() == Minecraft.getMinecraft().player && e.getPartialTicks() != 1 && Minecraft.getMinecraft().player.ridingEntity == null) {
            e.setRenderYawOffset(interpolateAngle(e.getPartialTicks(), prevYaw, yaw));
            e.setRenderHeadYaw(interpolateAngle(e.getPartialTicks(), prevYaw, yaw) - e.getRenderYawOffset());
            e.setRotationYawHead(interpolateAngle(e.getPartialTicks(), prevYaw, yaw));
            e.setRenderHeadPitch(lerp(e.getPartialTicks(), prevPitch, pitch));
        }
    }
    public static float interpolateAngle(float p_219805_0_, float p_219805_1_, float p_219805_2_) {
        return p_219805_1_ + p_219805_0_ * MathUtil.wrapAngleTo180_float(p_219805_2_ - p_219805_1_);
    }

    public static float lerp(float pct, float start, float end) {
        return start + pct * (end - start);
    }
}
