package net.meander.subtlyd.mixin.client.options.controls;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.meander.subtlyd.client.OptionsSD;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ControlsScreen.class)
public class ControlsScreenMixin {
    /**
     * Adds new accessibility options.
     */
    @Inject(method = "options", at = @At("RETURN"), cancellable = true)
    private static void options(Options options, CallbackInfoReturnable<OptionInstance<?>[]> cir) {
        List<OptionInstance<?>> optionInstanceList = new ArrayList<>(List.of(cir.getReturnValue().clone()));
        optionInstanceList.add(3, new OptionsSD().shieldCrouch());
        cir.setReturnValue(optionInstanceList.toArray(new OptionInstance[0]));
    }
}