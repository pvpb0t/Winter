package cc.winterclient.client.forge;

import cc.winterclient.client.event.ext.render.EventRender2D;
import cc.winterclient.client.event.ext.render.EventRender3D;
import cc.winterclient.client.event.ext.render.EventRenderWorldLast;
import cc.winterclient.client.event.ext.scenarios.EventKey;
import cc.winterclient.client.event.ext.scenarios.EventLogin;
import cc.winterclient.client.event.ext.scenarios.EventLogout;
import cc.winterclient.client.event.ext.update.EventTick;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;

public class ForgeEventInjector {

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Text event){
        EventRender2D eventRender2D = new EventRender2D();
        eventRender2D.call();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @SubscribeEvent
    public void onTick(TickEvent event){
        EventTick eventTick = new EventTick();
        eventTick.call();
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (event.isCanceled())
            return;
        EventRenderWorldLast eventRenderWorldLast = new EventRenderWorldLast();
        eventRenderWorldLast.call();
        Minecraft.getMinecraft().profiler.startSection("ketamine");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth(1.0F);
        EventRender3D eee = new EventRender3D(event.getPartialTicks());
        eee.call();
        GlStateManager.glLineWidth(1.0F);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        Minecraft.getMinecraft().profiler.endSection();
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event){
        if(Keyboard.getEventKeyState()) {
            EventKey e = new EventKey(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey());
            e.call();
        }
    }

    @SubscribeEvent
    public void onLogin(FMLNetworkEvent.ClientConnectedToServerEvent  event){
        EventLogin e = new EventLogin();
        e.call();
    }

    @SubscribeEvent
    public void onLogout(FMLNetworkEvent.ClientDisconnectionFromServerEvent event){
        EventLogout e = new EventLogout();
        e.call();
    }

}
