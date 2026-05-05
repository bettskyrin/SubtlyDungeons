package net.meander.subtlyd.mixin.client.renderer.item;

import net.meander.subtlyd.client.renderer.ChargedTridentState;
import net.meander.subtlyd.world.item.TridentSD;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelResolver.class)
public class ItemModelResolverMixin {
    @Inject(method = "updateForTopItem", at = @At("TAIL"))
    private void updateTridentAura(ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, Level level, ItemOwner owner, int seed, CallbackInfo ci) {
        if (item.is(Items.TRIDENT)) {
            if (owner instanceof LivingEntity livingEntity) {
                if (livingEntity.getTicksUsingItem() >= TridentSD.CHANNELING_CHARGE_TIME && TridentSD.canChargeChanneling(level, livingEntity, item)) {
                    ((ChargedTridentState.Accessor) output).subtlyDungeons$setCharged(true);
                    return;
                }
            }
            ((ChargedTridentState.Accessor) output).subtlyDungeons$setCharged(false);
        }
    }
}
