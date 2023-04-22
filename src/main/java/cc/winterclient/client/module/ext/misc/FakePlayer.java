package cc.winterclient.client.module.ext.misc;

import cc.winterclient.client.module.Category;
import cc.winterclient.client.module.Module;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.UUID;

public class FakePlayer extends Module {

    public EntityOtherPlayerMP entityPlayer;

    public FakePlayer() {
        super("FakePlayer", Category.MISC, 0);
    }

    @Override
    public void onEnable(){
        super.onEnable();
        entityPlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.randomUUID(), "MegaGay420"));
        entityPlayer.copyLocationAndAnglesFrom(mc.player);
        entityPlayer.rotationYawHead = mc.player.rotationYawHead;
        entityPlayer.setHealth(36);
        entityPlayer.entityId = -69;
        mc.world.spawnEntity(entityPlayer);
    }

    //Too lazy to make damage calculations etc...

    @Override
    public void onDisable(){
        super.onDisable();

        mc.world.removeEntity(entityPlayer);
    }



}

