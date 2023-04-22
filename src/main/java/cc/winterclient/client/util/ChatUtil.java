package cc.winterclient.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class ChatUtil {

    private static Minecraft mc = Minecraft.getMinecraft();

    public static void sendMessage(String message){
        mc.player.sendMessage(new TextComponentString("§7[§b§lWinter§r§7] §r" + message));
    }

}
