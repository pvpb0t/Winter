package cc.winterclient.client.module.ext.visual;

import cc.winterclient.client.event.EventTarget;
import cc.winterclient.client.event.Priority;
import cc.winterclient.client.event.ext.render.EventRender3D;
import cc.winterclient.client.event.ext.render.EventRenderWorldFirst;
import cc.winterclient.client.event.ext.render.EventRenderWorldLast;
import cc.winterclient.client.event.ext.update.EventPre;
import cc.winterclient.client.event.ext.update.EventTick;
import cc.winterclient.client.module.Category;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.option.Option;
import cc.winterclient.client.option.ext.BoolOption;
import cc.winterclient.client.option.ext.DoubleOption;
import cc.winterclient.client.util.Wrapper;
import cc.winterclient.client.util.logger.Logger;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static cc.winterclient.client.RotationAnimator.interpolateAngle;
import static cc.winterclient.client.util.Wrapper.mc;

public class Visuals extends Module{
    public Option<Boolean> transSelf = new BoolOption("TransparentSelf", true, this);
    public Option<Double> transThirdPersonOpacity = new DoubleOption("SelfOpacity", 1D, 0D, 1D, this, false, v -> transSelf.getExact());

    public Option<Boolean> transother = new BoolOption("TransparentClose", true, this);
    public Option<Double> transOtherPerson = new DoubleOption("EntityOpacity", 1D, 0D, 1D, this, false, v -> transother.getExact());
    public Option<Double> rangeOther = new DoubleOption("Range", 1.5D, 0D, 4D, this, false, v -> transother.getExact());
    public Option<Boolean> showFake = new BoolOption("ServerSideCham", true, this);
    public Option<Double> fakeRed = new DoubleOption("ServerRed", 255D, 0D, 255D, this, false, v -> showFake.getExact());
    public Option<Double> fakeGreen = new DoubleOption("ServerGreen", 255D, 0D, 255D, this, false, v -> showFake.getExact());
    public Option<Double> fakeBlue = new DoubleOption("ServerBlue", 255D, 0D, 255D, this, false, v -> showFake.getExact());
    public Option<Double> fakeAlpha = new DoubleOption("ServerAlpha", 155D, 0D, 255D, this, false, v -> showFake.getExact());
    EntityOtherPlayerMP player;
    ModelPlayer playerModel;
    private static float yaw;
    private float pitch;
    private static float prevYaw;
    private float prevPitch;


    public static Visuals instance;

    public Visuals(){
        super("Visuals", "Changes the way you render things", Category.VISUAL, Keyboard.KEY_NONE, false);
        instance = this;
    }

    @Override
    public void onEnable(){
        if(showFake.getExact()) {
            createFake();
        }
        super.onEnable();

    }

    private void createFake(){
        this.playerModel = new ModelPlayer(0.0f, false);
        this.playerModel.bipedHead.showModel = false;
        this.playerModel.bipedBody.showModel = false;
        this.playerModel.bipedLeftArmwear.showModel = false;
        this.playerModel.bipedLeftLegwear.showModel = false;
        this.playerModel.bipedRightArmwear.showModel = false;
        this.playerModel.bipedRightLegwear.showModel = false;
        (this.player = new EntityOtherPlayerMP((World) this.mc.world, mc.player.getGameProfile())).copyLocationAndAnglesFrom(mc.player);
    }

    @EventTarget(value = Priority.FIFTH)
    public void onPre(EventPre e){
        if(nullCheck()) return;
        if(showFake.getExact()) {
            createFake();
            player.setRotationYawHead(e.getYaw());
            player.rotationYaw = e.getYaw();
            player.rotationPitch = e.getPitch();
            player.setSneaking(mc.player.isSneaking());
            yaw = e.getYaw();
            pitch = e.getPitch();
        }
    }

    @EventTarget
    public void onTick(EventTick e){
        prevYaw = yaw;
        prevPitch = pitch;
    }

    @EventTarget
    public void onRender(EventRenderWorldFirst e){
        if(nullCheck() || player == null){
            return;
        }
        if(showFake.getExact()) {
            if (mc.gameSettings.thirdPersonView != 0) {
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.glLineWidth(1.0F);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.disableLighting();
                GlStateManager.disableCull();
                GlStateManager.enableAlpha();
                GlStateManager.color((float) (fakeRed.getExact() / 255f), (float) (fakeGreen.getExact() / 255), (float) (fakeBlue.getExact() / 255f), (float) (fakeAlpha.getExact() / 255));
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
                renderEntity(player, playerModel, mc.player.limbSwing, mc.player.limbSwingAmount, mc.player.ticksExisted, player.rotationYaw, player.rotationPitch, 1, e.getPartialTicks());
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
                GlStateManager.enableCull();
                GlStateManager.depthMask(true);
                GlStateManager.enableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.enableDepth();
            }
        }
    }

    public static void renderEntity (EntityOtherPlayerMP entity, ModelPlayer modelBase, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, float partialtick ) {

        float partialTicks = partialtick;
        double x = mc.player.posX - mc.getRenderManager( ).viewerPosX;
        double y = mc.player.posY - mc.getRenderManager( ).viewerPosY;
        double z = mc.player.posZ - mc.getRenderManager( ).viewerPosZ;
        double interpolatedX = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * mc.timer.renderPartialTicks - mc.renderManager.viewerPosX;
        double interpolatedY = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * mc.timer.renderPartialTicks - mc.renderManager.viewerPosY;
        double interpolatedZ = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * mc.timer.renderPartialTicks - mc.renderManager.viewerPosZ;
        GlStateManager.pushMatrix( );

        if ( entity.isSneaking( ) ) {
            y -= 0.125D;
        }
        GlStateManager.translate( ( float ) interpolatedX, ( float ) interpolatedY, ( float ) interpolatedZ );
        float f = interpolateRotation(prevYaw, yaw, partialTicks);
        float f1 = interpolateRotation(prevYaw, yaw, partialTicks);
        float yawz = f1 - f; // Forge: Fix MC-1207
        GlStateManager.rotate( 180 - f, 0, 1, 0 );
        float f4 = prepareScale( entity, scale );
        //float yaw = interpolateAngle(partialtick, prevYaw, entity.rotationYaw) - entity.renderYawOffset;



        GlStateManager.enableAlpha( );
        modelBase.setLivingAnimations( entity, limbSwing, limbSwingAmount, partialTicks );
        modelBase.setRotationAngles( limbSwing, limbSwingAmount, 0, yawz, entity.rotationPitch, f4, entity );
        modelBase.render( entity, limbSwing, limbSwingAmount, 0, yawz, entity.rotationPitch, f4 );

        GlStateManager.popMatrix( );
    }

    public static float prepareScale(final EntityLivingBase entity, final float scale) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        final double widthX = entity.getRenderBoundingBox().maxX - entity.getRenderBoundingBox().minX;
        final double widthZ = entity.getRenderBoundingBox().maxZ - entity.getRenderBoundingBox().minZ;
        GlStateManager.scale(scale + widthX, (double)(scale * entity.height), scale + widthZ);
        final float f = 0.0625f;
        GlStateManager.translate(0.0f, -1.501f, 0.0f);
        return 0.0625f;
    }

    protected static float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks)
    {
        float f;

        for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F)
        {
            ;
        }

        while (f >= 180.0F)
        {
            f -= 360.0F;
        }

        return prevYawOffset + partialTicks * f;
    }


    @Override
    public void onDisable(){
        super.onDisable();

    }


}