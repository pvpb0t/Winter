package cc.winterclient.client.forge;

import cc.winterclient.client.util.logger.Logger;
import cc.winterclient.client.Winter;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Winter.modid, name = Winter.clientName, version = Winter.clientVersion)
public class ForgeHandler {

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Logger.getLogger().print("bruh moment");
      new Winter().startClient();
    }



}
