package cc.winterclient.client.module.ext.movement;

import cc.winterclient.client.event.EventTarget;
import cc.winterclient.client.event.ext.update.EventPre;
import cc.winterclient.client.event.ext.scenarios.EventReceivePacket;
import cc.winterclient.client.module.Category;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.option.Option;
import cc.winterclient.client.option.ext.BoolOption;
import cc.winterclient.client.option.ext.DoubleOption;
import cc.winterclient.client.option.ext.EnumOption;
import cc.winterclient.client.util.Timer;
import cc.winterclient.client.util.entity.player.MotionUtil;
import cc.winterclient.client.util.logger.Logger;
import cc.winterclient.client.util.world.BlockUtil;
import net.minecraft.block.BlockSlime;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.lwjgl.input.Keyboard;
import scala.tools.cmd.gen.AnyValReps;

import java.util.Arrays;
import java.util.List;

public class Flight extends Module {

    private static Minecraft mc = Minecraft.getMinecraft();
    private Timer timer = new Timer();

    private List<String> modes = Arrays.asList("Slime", "Vanilla", "Hypixel");
    private Option<String> mode = new EnumOption("Mode", "Vanilla", modes, this);

    //SLIME
    public Option<Double> down = new DoubleOption("DVelocity", 5D, 0D, 100D, this, true, v-> mode.getExact().equalsIgnoreCase("Slime"));
    public Option<Double> SlimeSpeed = new DoubleOption("Speed", 2.5D, 0D, 7D, this, false, v-> mode.getExact().equalsIgnoreCase("Slime"));
    public Option<Double> SlimeMaxTick = new DoubleOption("MaxTick", 2.5D, 0D, 5D, this, true, v-> mode.getExact().equalsIgnoreCase("Slime"));
    public Option<Double> SlimeFlyTime = new DoubleOption("FlyTimeFactor", 0.5D, 0D, 3D, this, false, v-> mode.getExact().equalsIgnoreCase("Slime"));
    public Option<Double> SlimeVSpeed = new DoubleOption("Speed", 2.5D, 0D, 5D, this, false, v-> mode.getExact().equalsIgnoreCase("Slime"));

    private Integer lock;
private Option<Boolean> keepalive = new BoolOption("KeepAlive", true, this);
    private Option<Boolean> onGround = new BoolOption("OnGround", true, this, v->  mode.getExact().equalsIgnoreCase("Slime"));
    private Option<Boolean> bypassSlow = new BoolOption("SlowBypass", true, this, v->  mode.getExact().equalsIgnoreCase("Slime"));

    private Option<Boolean> setback = new BoolOption("Setback", true, this);
    private Option<Boolean> damage = new BoolOption("Damage", false, this);
    private Option<Boolean> AntiKick = new BoolOption("AntiKick", true, this);

    public Flight() {
        super("Flight", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @Override
    public void onEnable() {
        if(damage.getExact()){
            Flight.damage();

        }
        lock=0;
        super.onEnable();

    }

    @Override
    public void onDisable() {
        lock=0;
        super.onDisable();

    }

    public static void damage() {
        double offset = 0.0625;
        if (mc.player != null && mc.player.onGround) {
            for (int i = 0; i <= (4 / offset); i++) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,
                        mc.player.posY + offset, mc.player.posZ, false));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,
                        mc.player.posY, mc.player.posZ, (i == (4 / offset))));
            }
        }
    }


    @EventTarget
    public void onPacketReceive(EventReceivePacket event) {
        if(setback.getExact()){
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            final SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            packet.yaw = mc.player.rotationYaw;
            packet.pitch = mc.player.rotationPitch;
            packet.x = mc.player.posX;
            packet.y = mc.player.posY - 1.0E-10D;
            packet.z = mc.player.posZ;
        }
        }

        if(event.getPacket() instanceof CPacketPlayer && lock > 0){
            mc.player.connection.sendPacket(new CPacketKeepAlive());

        }

    }

    @Override
    public String HudInfo(){
        if(mode.getExact().equalsIgnoreCase("slime")) {
            return String.valueOf(lock);
        }
        return null;
    }

    @EventTarget
    public void onPre(EventPre e) {
        if(mode.getExact().equalsIgnoreCase("slime")){
            if(BlockUtil.getStandingBlock() instanceof BlockSlime){
                mc.player.motionY = -(down.getExact());
                lock = SlimeMaxTick.getExact().intValue();
                Logger.getLogger().print("bro");
            }else if(lock> 0){
                lock -= SlimeFlyTime.getExact().intValue();
                mc.player.capabilities.isFlying = false;
                if(keepalive.getExact()){
                    mc.player.connection.sendPacket(new CPacketKeepAlive());
                }

                if(bypassSlow.getExact()){
                    MotionUtil.stopPlayerMovement();

                }else{
                    mc.player.motionY = 0;
                }

                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.player.motionY += SlimeVSpeed.getExact();
                }
                if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.player.motionY -= SlimeVSpeed.getExact();
                }
                MotionUtil.strafe((EventPre) null,SlimeSpeed.getExact());

            }


        }

        if(AntiKick.getExact()){
            if(!mc.player.onGround) {
                if (mc.player.ticksExisted % 10 == 0) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 0.2, mc.player.posY, mc.player.onGround));
                }
            }
        }


    }


}


