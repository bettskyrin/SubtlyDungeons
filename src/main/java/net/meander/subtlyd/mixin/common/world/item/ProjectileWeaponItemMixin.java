package net.meander.subtlyd.mixin.common.world.item;

import net.meander.subtlyd.world.item.QuiverItemSD;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileWeaponItem.class)
public abstract class ProjectileWeaponItemMixin {
    @Inject(method = "useAmmo", at = @At("HEAD"), cancellable = true)
    private static void prioritizeQuiver(ItemStack weapon, ItemStack projectile, LivingEntity holder, boolean forceInfinite, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack legItem = holder.getItemBySlot(EquipmentSlot.LEGS);

        if (legItem.getItem() instanceof QuiverItemSD) {
            ItemStack selectedArrow = QuiverItemSD.getActiveArrowAndCycle(legItem, weapon, holder, forceInfinite);
            
            if (!selectedArrow.isEmpty()) {
                cir.setReturnValue(selectedArrow);
            }
        }
    }
}