package cc.winterclient.client.util.entity.player;

import cc.winterclient.client.event.ext.update.EventPost;
import cc.winterclient.client.event.ext.update.EventPre;
import cc.winterclient.client.util.Wrapper;
import cc.winterclient.client.util.math.vec.Vec2D;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;

public class MotionUtil implements Wrapper {

    public static double interpolate(double start, double end) {
        float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        if (partialTicks == 1.0f) {
            return start;
        }

        return end + (start - end) * partialTicks;
    }

    public static boolean isMoving(EntityLivingBase entity) {
        return entity.moveStrafing != 0 || entity.moveForward != 0;
    }

    public static Vec2D strafe(double motionSpeed) {
        float forward = mc.player.movementInput.moveForward;
        float strafe = mc.player.movementInput.moveStrafe;
        float yaw = (float) interpolate(mc.player.rotationYaw, mc.player.prevRotationYaw);

        if (forward != 0.0f) {
            if (strafe > 0.0f) {
                yaw += forward > 0.0f ? -45.0f : 45.0f;
            } else if (strafe < 0.0f) {
                yaw += forward > 0.0f ? 45.0f : -45.0f;
            }

            strafe = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }

        double rad = Math.toRadians(yaw);

        double sin = -Math.sin(rad);
        double cos = Math.cos(rad);

        return new Vec2D(
                forward * motionSpeed * sin + strafe * motionSpeed * cos,
                forward * motionSpeed * cos - strafe * motionSpeed * sin
        );
    }

    public static void strafe(EventPre event, double moveSpeed) {
        Vec2D vec = strafe(moveSpeed);

        if (event != null) {
            event.setPosX(mc.player.motionX = vec.getX());
            event.setPosZ(mc.player.motionZ = vec.getZ());
        } else {
            mc.player.motionX = vec.getX();
            mc.player.motionZ = vec.getZ();
        }
    }

    public static void StrafeNoEvent(double moveSpeed) {
        Vec2D vec = strafe(moveSpeed);

            mc.player.motionX = vec.getX();
            mc.player.motionZ = vec.getZ();

    }


    public static void strafe(EventPost event, double moveSpeed) {
        Vec2D vec = strafe(moveSpeed);

        if (event != null) {
            event.setPosX(mc.player.motionX = vec.getX());
            event.setPosZ(mc.player.motionZ = vec.getZ());
        } else {
            mc.player.motionX = vec.getX();
            mc.player.motionZ = vec.getZ();
        }
    }

    public static void stopPlayerMovement(){
        mc.player.motionX = 0;
        mc.player.motionZ = 0;
        mc.player.motionY = 0;
    }

    /**
     * Gets the base NCP speed
     * @return the base NCP speed
     */
    public static double getBaseNCPSpeed() {
        double baseSpeed = 0.2873;

        if (mc.player == null) {
            return baseSpeed;
        }

        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            baseSpeed *= 1.0 + 0.2 * (mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier() + 1);
        }

        return baseSpeed;
    }

}
