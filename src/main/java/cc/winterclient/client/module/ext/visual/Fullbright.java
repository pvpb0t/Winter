package cc.winterclient.client.module.ext.visual;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import cc.winterclient.client.event.EventTarget;
import cc.winterclient.client.event.ext.update.EventUpdate;
import cc.winterclient.client.module.Category;
import cc.winterclient.client.module.Module;

public class Fullbright extends Module {


    public Fullbright() {
        super("Fullbright", Category.VISUAL, 0);
    }

    @EventTarget
    public void onUpdate(EventUpdate e){
        mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 1600));
    }

}
