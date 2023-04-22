package cc.winterclient.client.injection.mixins;

import cc.winterclient.client.event.ext.scenarios.EventArrowLoose;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.item.ItemBow.getArrowVelocity;

@Mixin(ItemBow.class)
public abstract class MixinItemBow {

    @Shadow public abstract int getMaxItemUseDuration(ItemStack stack);

    @Shadow protected abstract ItemStack findAmmo(EntityPlayer player);

    @Inject(method = "onPlayerStoppedUsing", at = @At("RETURN"))
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft, CallbackInfo ci){
        if(stack.getItem() instanceof ItemBow || stack.getItem() instanceof ItemArrow){
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            int i = getMaxItemUseDuration(stack) - timeLeft;
            boolean flag = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;

            ItemStack itemstack = findAmmo(entityplayer);


            float f = getArrowVelocity(net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, entityplayer, i, !itemstack.isEmpty() || flag));

            EventArrowLoose event = new EventArrowLoose(f);
            event.call();

        }
    }


}
