package net.meander.subtlyd.mixin.client.gui;

import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.client.gui.BundleMouseActions;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleMouseActions.class)
public class BundleMouseActionsMixin {
    @Inject(method = "matches", at = @At("RETURN"), cancellable = true)
    public void matches(Slot slot, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || slot.getItem().is(ItemsSD.QUIVER));
    }
}
