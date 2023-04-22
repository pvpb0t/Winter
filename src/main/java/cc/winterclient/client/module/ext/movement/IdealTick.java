package cc.winterclient.client.module.ext.movement;

import cc.winterclient.client.event.EventTarget;
import cc.winterclient.client.event.ext.scenarios.EventReceivePacket;
import cc.winterclient.client.event.ext.update.EventPre;
import cc.winterclient.client.module.Category;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.option.Option;
import cc.winterclient.client.option.ext.BoolOption;
import cc.winterclient.client.option.ext.DoubleOption;
import cc.winterclient.client.util.entity.player.MotionUtil;
import cc.winterclient.client.util.math.vec.Vec2D;
import net.minecraft.entity.MoverType;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.lwjgl.input.Keyboard;

public class IdealTick extends Module {
    public Option<Double> speed = new DoubleOption("Speed", 0.5D, 0D, 2D, this, false);
    private Option<Boolean> onground = new BoolOption("OnGround", true, this);

    public IdealTick(){
        super("IdealTick", "Idealtick, yk what it is", Category.MOVEMENT, Keyboard.KEY_NONE, false);
    }

    private int teleportID;

    @EventTarget
    public void onRecive(EventReceivePacket e){
        if (e.getPacket() instanceof SPacketPlayerPosLook) {
            this.teleportID = ((SPacketPlayerPosLook)e.getPacket()).teleportId;
        }
    }

    @EventTarget
    public void onPre(EventPre e){
        if(nullCheck() || !MotionUtil.isMoving(mc.player)){
            return;
        }
        Vec2D Boost = MotionUtil.strafe(speed.getExact());
        mc.player.move(MoverType.PLAYER, Boost.getX(), -0.01D, Boost.getZ());
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, onground.getExact()));
        mc.player.connection.sendPacket(new CPacketConfirmTeleport(teleportID++));

    }
    @Override
    public void onEnable() {
        super.onEnable();

    }

    @Override
    public void onDisable() {
        super.onDisable();

    }

}
