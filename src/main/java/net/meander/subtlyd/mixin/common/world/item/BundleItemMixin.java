package net.meander.subtlyd.mixin.common.world.item;

import net.meander.subtlyd.world.item.QuiverItemSD;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleItem.class)
public abstract class BundleItemMixin {
@Inject(method = "getFullnessDisplay", at = @At("HEAD"), cancellable = true)
    private static void adjustQuiverFullness(ItemStack itemStack, CallbackInfoReturnable<Float> cir) {
        if (itemStack.getItem() instanceof QuiverItemSD) {
            cir.setReturnValue(QuiverItemSD.getFullnessDisplay(itemStack));
        }
    }
}