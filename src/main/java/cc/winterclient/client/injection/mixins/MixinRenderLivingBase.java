package cc.winterclient.client.injection.mixins;

import cc.winterclient.client.event.ext.render.EventModelRotation;
import cc.winterclient.client.event.ext.render.EventPostRenderLiving;
import cc.winterclient.client.event.ext.render.EventPreRenderLiving;
import cc.winterclient.client.module.ext.visual.Visuals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(RenderLivingBase.class)
public class MixinRenderLivingBase{


    @Inject(method = "renderModel", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void renderModel(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, CallbackInfo ci) {
        if (Visuals.instance.isEnabled()) {

            if (entitylivingbaseIn instanceof EntityPlayerSP) {
                if (Visuals.instance.transSelf.getExact()) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, Visuals.instance.transThirdPersonOpacity.getExact().floatValue());
                    GlStateManager.depthMask(false);
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                    GlStateManager.alphaFunc(516, 0.003921569F);
                }
            } else if (Visuals.instance.transother.getExact() && Minecraft.getMinecraft().player.getDistance(entitylivingbaseIn) <= Visuals.instance.rangeOther.getExact()) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, Visuals.instance.transOtherPerson.getExact().floatValue());
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                GlStateManager.alphaFunc(516, 0.003921569F);
            }

        }
    }



}
