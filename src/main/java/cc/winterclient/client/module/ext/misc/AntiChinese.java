package cc.winterclient.client.module.ext.misc;

import cc.winterclient.client.event.EventTarget;
import cc.winterclient.client.event.ext.scenarios.EventReceivePacket;
import cc.winterclient.client.module.Category;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.util.logger.Logger;
import net.minecraft.network.play.server.SPacketChat;

public class AntiChinese extends Module {


    public AntiChinese(String name, String description, Category category, int key, Boolean hidden) {
        super(name, description, category, key, hidden);
    }


    @Override
    public void onEnable(){
        super.onEnable();

    }

    @EventTarget
    public void onRecive(EventReceivePacket e){
        if(e.getPacket() instanceof SPacketChat){
            String chat = ((SPacketChat) e.getPacket()).chatComponent.getUnformattedComponentText();
            if(chat.contains("縁")||chat.contains("贁")||chat.contains("ခᄁሁጁᐁᔁᘁᜁ")||chat.contains("āȁ́Ёԁ")||chat.contains("鰁鴁")){
                e.setCancelled(true);
                Logger.getLogger().printToChat("Someone tried to chinese lag you");
            }
        }
    }

    @Override
    public void onDisable(){
        super.onDisable();

    }


}
