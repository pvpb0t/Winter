package cc.winterclient.client.injection.mixins;

import cc.winterclient.client.event.ext.scenarios.EventPush;
import cc.winterclient.client.event.ext.scenarios.EventSendChat;
import cc.winterclient.client.event.ext.update.EventPost;
import cc.winterclient.client.event.ext.update.EventPre;
import cc.winterclient.client.event.ext.update.EventPreExtended;
import cc.winterclient.client.event.ext.update.EventUpdate;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

    @Shadow public abstract void setSprinting(boolean sprinting);

    @Shadow public abstract boolean isSneaking();

    private double cachedX;
    private double cachedY;
    private double cachedZ;

    private float cachedRotationPitch;
    private float cachedRotationYaw;

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(method = {"onUpdateWalkingPlayer"}, at = {@At(value = "HEAD")})
    private void preMotion(CallbackInfo info) {
        EventUpdate eventUpdate = new EventUpdate();
        eventUpdate.call();

        EventPreExtended eventPreExtended = new EventPreExtended(limbSwing, limbSwingAmount, onGround, isSprinting(), isSneaking());
        eventPreExtended.call();

        limbSwing = eventPreExtended.getLimbswing();
        limbSwingAmount= eventPreExtended.getLimbswingAmmount();


        cachedX = posX;
        cachedY = posY;
        cachedZ = posZ;

        cachedRotationYaw = rotationYaw;
        cachedRotationPitch = rotationPitch;

        EventPre e = new EventPre(posX, posY, posZ, rotationYaw, rotationPitch, onGround, isSprinting(), isSneaking());
        e.call();

        posX = e.getPosX();
        posY = e.getPosY();
        posZ = e.getPosZ();


        onGround = e.isOnGround();
        setSprinting(e.isSprinting());
        setSneaking(e.isSneaking());

        rotationYaw = e.getYaw();
        rotationPitch = e.getPitch();
    }

    @Inject(method = {"onUpdateWalkingPlayer"}, at = {@At(value = "RETURN")})
    private void postMotion(CallbackInfo info) {
        EventPost e = new EventPost(posX, posY, posZ, rotationYaw, rotationPitch, onGround, isSprinting(), isSneaking());
        e.call();
        posX = e.getPosX();
        posY = e.getPosY();
        posZ = e.getPosZ();

        onGround = e.isOnGround();
        setSprinting(e.isSprinting());
        setSneaking(e.isSneaking());

        rotationYaw = e.getYaw();
        rotationPitch = e.getPitch();
        posX = cachedX;
        posY = cachedY;
        posZ = cachedZ;

        rotationYaw = cachedRotationYaw;
        rotationPitch = cachedRotationPitch;
    }

    @Inject(method={"pushOutOfBlocks"}, at={@At(value="HEAD")}, cancellable=true)
    private void pushOutOfBlocksHook(double x, double y, double z, CallbackInfoReturnable<Boolean> info) {
        EventPush event = new EventPush();
        event.call();
        if (event.isCancelled()) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "sendChatMessage", at=@At("HEAD"), cancellable = true)
    private void onChat(String message, CallbackInfo callbackInfo){
        EventSendChat event = new EventSendChat(message);
        event.call();
        if(event.isCancelled())
        {
            callbackInfo.cancel();
        }
    }

}
