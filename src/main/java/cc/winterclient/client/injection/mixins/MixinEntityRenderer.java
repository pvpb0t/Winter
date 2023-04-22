package cc.winterclient.client.injection.mixins;

import cc.winterclient.client.event.ext.render.EventRenderWorldFirst;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    //8
    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", ordinal = 9))
    private void renderWorldFirst(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci){
        Minecraft.getMinecraft().profiler.endStartSection("winter_render_first");
        EventRenderWorldFirst eventRenderWorldFirst = new EventRenderWorldFirst(Minecraft.getMinecraft().renderGlobal, partialTicks);
        eventRenderWorldFirst.call();
    }

}
