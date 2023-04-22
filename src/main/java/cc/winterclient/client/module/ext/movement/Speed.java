package cc.winterclient.client.module.ext.movement;

import cc.winterclient.client.event.EventTarget;
import cc.winterclient.client.event.ext.scenarios.EventReceivePacket;
import cc.winterclient.client.event.ext.update.EventPost;
import cc.winterclient.client.event.ext.update.EventPre;
import cc.winterclient.client.event.ext.scenarios.EventSendPacket;
import cc.winterclient.client.module.Category;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.option.Option;
import cc.winterclient.client.option.ext.BoolOption;
import cc.winterclient.client.option.ext.DoubleOption;
import cc.winterclient.client.option.ext.EnumOption;
import cc.winterclient.client.util.entity.player.MotionUtil;
import cc.winterclient.client.util.math.vec.Vec2D;
import net.minecraft.entity.MoverType;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

public class Speed extends Module{

    private List<String> modes = Arrays.asList("AirStrafe", "Vanilla2");
    private Option<String> mode = new EnumOption("Mode", "AirStrafe", modes, this);

    //Airstrafe
    private Option<Boolean> jump = new BoolOption("Jump", true, this, v-> mode.getExact().equalsIgnoreCase("airstrafe"));
    public Option<Double> jumpFactor = new DoubleOption("JumpFactor", 1D, 0D, 2D, this, false,v-> mode.getExact().equalsIgnoreCase("airstrafe"));


    //Vanilla2
    public Option<Double> speed = new DoubleOption("Speed", 0.5D, 0D, 2D, this, false,v-> mode.getExact().equalsIgnoreCase("Vanilla2"));
    private Option<Boolean> onground = new BoolOption("OnGround", true, this,v-> mode.getExact().equalsIgnoreCase("Vanilla2"));
    private int teleportID;


    private Option<Boolean> setback = new BoolOption("Setback", true, this);
    private Option<Boolean> conFirm = new BoolOption("ComfirmTP", true, this);

        public Speed(){
            super("Speed", Category.MOVEMENT, Keyboard.KEY_NONE);
        }


    @EventTarget
    public void onRecive(EventReceivePacket e){

                if (e.getPacket() instanceof SPacketPlayerPosLook) {
                     this.teleportID = ((SPacketPlayerPosLook)e.getPacket()).teleportId;


                    if(setback.getExact()){
                        final SPacketPlayerPosLook packet = (SPacketPlayerPosLook) e.getPacket();
                        packet.yaw = mc.player.rotationYaw;
                        packet.pitch = mc.player.rotationPitch;
                        packet.x = mc.player.posX;
                        packet.y = mc.player.posY - 1.0E-10D;
                        packet.z = mc.player.posZ;
                    }

                }
    }

        @Override
        public void onEnable(){

            teleportID=0;
            super.onEnable();
        }


        @EventTarget
        public void onPre(EventPre e){

            if(mode.getExact().equalsIgnoreCase("Vanilla2")){
                if(nullCheck() || !MotionUtil.isMoving(mc.player)){
                    return;
                }
                Vec2D Boost = MotionUtil.strafe(speed.getExact());
                mc.player.move(MoverType.PLAYER, Boost.getX(), -0.01D, Boost.getZ());
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, onground.getExact()));
                mc.player.connection.sendPacket(new CPacketConfirmTeleport(teleportID++));
            }else if(mode.getExact().equalsIgnoreCase("airstrafe")) {
                if(MotionUtil.isMoving(mc.player)){
                    if (!mc.player.onGround) {
                        MotionUtil.strafe((EventPre) null, MotionUtil.getBaseNCPSpeed());
                    }else {
                        if(jump.getExact())
                        mc.player.motionY = 0.405 * jumpFactor.getExact();

                    }
                }
            }

        }

        @EventTarget
        public void onPost(EventPost e){
            if(teleportID<=0){
                mc.player.connection.sendPacket(new CPacketConfirmTeleport(teleportID));

            }
        }

        @EventTarget
        public void onSend(EventSendPacket e){
        }

        @Override
        public void onDisable(){
            teleportID=0;

            super.onDisable();
        }



    }



