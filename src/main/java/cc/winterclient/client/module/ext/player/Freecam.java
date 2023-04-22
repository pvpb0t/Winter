package cc.winterclient.client.module.ext.player;

import cc.winterclient.client.option.Option;
import cc.winterclient.client.option.ext.BoolOption;
import cc.winterclient.client.option.ext.DoubleOption;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import cc.winterclient.client.event.EventTarget;
import cc.winterclient.client.event.ext.scenarios.EventPush;
import cc.winterclient.client.event.ext.scenarios.EventReceivePacket;
import cc.winterclient.client.event.ext.scenarios.EventSendPacket;
import cc.winterclient.client.event.ext.update.EventUpdate;
import cc.winterclient.client.module.Category;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.util.math.MathUtil;

public class Freecam extends Module {

    private Option<Double> speed = new DoubleOption("Speed", 1D, 0D, 10D, this, false);
    private Option<Boolean> cancelPackets = new BoolOption("Cancel Packets", true, this);

    private AxisAlignedBB oldBoundingBox;
    private EntityOtherPlayerMP entity;
    private Vec3d position;
    private Entity riding;
    private float yaw;
    private float pitch;

    public Freecam() {
        super("Freecam", Category.PLAYER, 0);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        oldBoundingBox = mc.player.getEntityBoundingBox();
        mc.player.setEntityBoundingBox(new AxisAlignedBB(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.posX, mc.player.posY, mc.player.posZ));
        if (mc.player.getRidingEntity() != null) {
            riding = mc.player.getRidingEntity();
            mc.player.dismountRidingEntity();
        }
        entity = new EntityOtherPlayerMP(mc.world, mc.session.getProfile());
        entity.copyLocationAndAnglesFrom((Entity) mc.player);
        entity.rotationYaw = mc.player.rotationYaw;
        entity.rotationYawHead = mc.player.rotationYawHead;
        entity.inventory.copyInventory(mc.player.inventory);
        mc.world.addEntityToWorld(-6666666, (Entity) entity);
        position = mc.player.getPositionVector();
        yaw = mc.player.rotationYaw;
        pitch = mc.player.rotationPitch;
        mc.player.noClip = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.player.setEntityBoundingBox(oldBoundingBox);
        if (riding != null) {
            mc.player.startRiding(riding, true);
        }
        if (entity != null) {
            mc.world.removeEntity((Entity) entity);
        }
        if (position != null) {
            mc.player.setPosition(position.x, position.y, position.z);
        }
        mc.player.rotationYaw = yaw;
        mc.player.rotationPitch = pitch;
        mc.player.noClip = false;
    }


    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.player.noClip = true;
        mc.player.setVelocity(0.0, 0.0, 0.0);
        mc.player.jumpMovementFactor = speed.getExact().floatValue();
        double[] dir = MathUtil.directionSpeed(speed.getExact());
        if (mc.player.movementInput.moveStrafe != 0.0f || mc.player.movementInput.moveForward != 0.0f) {
            mc.player.motionX = dir[0];
            mc.player.motionZ = dir[1];
        } else {
            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;
        }
        mc.player.setSprinting(false);
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.motionY += speed.getExact();
        }
        if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.player.motionY -= speed.getExact();
        }
    }

    @EventTarget
    public void packetSend(EventSendPacket event){
        if(event.getPacket() instanceof CPacketPlayer && this.entity != null){
            CPacketPlayer packetPlayer = (CPacketPlayer)event.getPacket();
            packetPlayer.x = this.entity.posX;
            packetPlayer.y = this.entity.posY;
            packetPlayer.z = this.entity.posZ;
            return;
        }
        if(this.cancelPackets.getExact()){
            event.setCancelled(true);
        }else if(!(event.getPacket() instanceof CPacketUseEntity || event.getPacket() instanceof CPacketPlayerTryUseItem || event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock || event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketVehicleMove || event.getPacket() instanceof CPacketChatMessage || event.getPacket() instanceof CPacketKeepAlive))
        {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void packetReceive(EventReceivePacket event){
        SPacketSetPassengers sp;
        Entity r;
        if (event.getPacket() instanceof SPacketSetPassengers && (r = mc.world.getEntityByID((sp = (SPacketSetPassengers)event.getPacket()).getEntityId())) != null && r == this.riding) {
            this.riding = null;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            if (!this.cancelPackets.getExact()) {
                if (this.entity != null) {
                    this.entity.setPositionAndRotation(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch());
                }
                this.position = new Vec3d(packet.getX(), packet.getY(), packet.getZ());
                mc.player.connection.sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));
                event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onPush(EventPush e){
        e.setCancelled(true);
    }

}
