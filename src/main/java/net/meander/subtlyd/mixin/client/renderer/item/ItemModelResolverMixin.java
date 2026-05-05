package net.meander.subtlyd.mixin.client.renderer.item;

import net.meander.subtlyd.client.renderer.ChargedTridentState;
import net.meander.subtlyd.world.inventory.AnvilMenuSD;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelResolver.class)
public class ItemModelResolverMixin {
    @Inject(method = "updateForLiving", at = @At("TAIL"))
    private void extractTridentCharge(ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, LivingEntity entity, CallbackInfo ci) {
        if (entity.getTicksUsingItem() >= 60) {
            if (entity.getUseItem().is(Items.TRIDENT) && AnvilMenuSD.checkEnchantment(item, Enchantments.CHANNELING) && entity.level().canHaveWeather()) {
                ((ChargedTridentState.Accessor) output).subtlyDungeons$setCharged(true);
                return;
            }
        }
        ((ChargedTridentState.Accessor) output).subtlyDungeons$setCharged(false);
    }
}
