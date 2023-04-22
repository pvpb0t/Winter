package cc.winterclient.client.util;

import net.minecraft.client.Minecraft;

public interface Wrapper {
    Minecraft mc = Minecraft.getMinecraft();

    default boolean nullCheck() {
        return mc.world == null || mc.player == null;
    }
}
