package cc.winterclient.client.module.ext.client;

import cc.winterclient.client.Winter;
import cc.winterclient.client.event.EventTarget;
import cc.winterclient.client.event.ext.scenarios.EventReceivePacket;
import cc.winterclient.client.event.ext.render.EventRender2D;
import cc.winterclient.client.event.ext.scenarios.EventSendPacket;
import cc.winterclient.client.event.ext.update.EventUpdate;
import cc.winterclient.client.font.FontUtil;
import cc.winterclient.client.module.Category;
import cc.winterclient.client.module.Module;

import cc.winterclient.client.option.Option;
import cc.winterclient.client.option.ext.BoolOption;
import cc.winterclient.client.option.ext.DoubleOption;
import net.minecraft.client.gui.ScaledResolution;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HUD extends Module {

    private Integer inCommingPackets;
    private Integer outGoingPacket;
    private String outGoing;
    private String inComing;

    private Option<Double> red = new DoubleOption("Red", 255D, 0D, 255D, this, true);
    private Option<Double> green = new DoubleOption("Green", 255D, 0D, 255D, this, true);
    private Option<Double> blue = new DoubleOption("Blue", 255D, 0D, 255D, this, true);
    private Option<Boolean> watermark = new BoolOption("Watermark", true, this);
    private Option<Boolean> arraylist = new BoolOption("Arraylist", true, this);
    private Option<Boolean> packetinfo = new BoolOption("PacketInfo", true, this);

    private Option<Double> renderOffset = new DoubleOption("Render Offset", 0D, 0D, 200D, this, true);


    public HUD() {
        super("HUD", Category.CLIENT, 0);
    }

    @EventTarget
    public void onRender(EventRender2D event) {
        if (watermark.getExact())
            renderWatermark();

        if (arraylist.getExact())
            renderArraylist();

        if(packetinfo.getExact())
            renderPacketInfo();

    }

    private void renderPacketInfo(){
        if(outGoing != null && inComing != null){
            Winter.getCustomFontRenderer().drawStringWithShadow("Incoming Packets: " + " [" + this.inComing + "]", 2, 40, new Color(red.getExact().intValue(), green.getExact().intValue(), blue.getExact().intValue()).getRGB());
            Winter.getCustomFontRenderer().drawStringWithShadow("OutGoing Packets: " + " [" + this.outGoing + "]", 2, 42 + FontUtil.getFontHeight(), new Color(red.getExact().intValue(), green.getExact().intValue(), blue.getExact().intValue()).getRGB());

        }
    }

    @EventTarget
    public void onRecive(EventReceivePacket e){
        inCommingPackets++;
    }

    @EventTarget
    public void onUpdate(EventUpdate e){
        if(mc.player.ticksExisted % 20 == 0){
            outGoing = String.valueOf(outGoingPacket);
            inComing = String.valueOf(inCommingPackets);

            inCommingPackets = 0;
            outGoingPacket =0;
        }
    }

    @EventTarget
    public void onSend(EventSendPacket e){
        outGoingPacket++;
    }

    private void renderArraylist() {
        final List<Module> activeModules = new ArrayList<>();
        final ScaledResolution sr = new ScaledResolution(mc);

        int moduleCounter = 2;
        int arrayListColor;
        for (Module m : Winter.instance.moduleManager.getModuleList()) {
            if (m.isEnabled() && !m.isHidden()) {
                activeModules.add(m);
            }
        }

        activeModules.sort((m1, m2) -> mc.fontRenderer.getStringWidth(m2.getDisplayName()) - mc.fontRenderer.getStringWidth(m1.getDisplayName()));

        arrayListColor = new Color(red.getExact().intValue(), green.getExact().intValue(), blue.getExact().intValue()).getRGB();

        for (Module m : activeModules) {
            int stringWidth = Winter.getCustomFontRenderer().getStringWidth(m.getDisplayName() + (m.HudInfo() == null? "" : "[" + m.HudInfo()+"]"));
            Winter.getCustomFontRenderer().drawStringWithShadow(m.getDisplayName() + (m.HudInfo() == null? "" : "[" + m.HudInfo()+"]"), sr.getScaledWidth() - stringWidth - 3 - renderOffset.getExact().intValue(), moduleCounter + renderOffset.getExact().intValue(), arrayListColor);
            moduleCounter += 12;
        }

    }

    private void renderWatermark() {
        Winter.getCustomFontRenderer().drawStringWithShadow(Winter.clientName + " [" + Winter.clientVersion + "]", 2, 2, new Color(red.getExact().intValue(), green.getExact().intValue(), blue.getExact().intValue()).getRGB());
    }

    @Override
    public void onEnable(){
        outGoingPacket = 0;
        inCommingPackets = 0;
        super.onEnable();
    }

    @Override
    public void onDisable(){
        outGoingPacket = 0;
        inCommingPackets = 0;
        super.onDisable();
    }
}
