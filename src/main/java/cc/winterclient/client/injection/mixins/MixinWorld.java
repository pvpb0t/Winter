package cc.winterclient.client.injection.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import cc.winterclient.client.event.ext.scenarios.EventPush;

@Mixin(World.class)
public class MixinWorld {


    @Redirect(method={"handleMaterialAcceleration"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;isPushedByWater()Z"))
    public boolean isPushedbyWaterHook(Entity entity) {
        EventPush event = new EventPush(entity);
        event.call();
        return entity.isPushedByWater() && !event.isCancelled();
    }
}
