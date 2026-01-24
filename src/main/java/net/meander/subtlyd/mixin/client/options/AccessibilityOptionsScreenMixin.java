package net.meander.subtlyd.mixin.client.options;

import net.meander.subtlyd.client.OptionInstanceSD;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(AccessibilityOptionsScreen.class)
public class AccessibilityOptionsScreenMixin {
    /**
     * Adds Screen Shake accessibility option.
     */
    @Inject(method = "options", at = @At("RETURN"), cancellable = true)
    private static void options(Options options, CallbackInfoReturnable<OptionInstance<?>[]> cir) {
        List<OptionInstance<?>> optionInstanceList = new ArrayList<>(List.of(cir.getReturnValue().clone()));
        optionInstanceList.add(18, new OptionInstanceSD().screenShake());
        cir.setReturnValue(optionInstanceList.toArray(new OptionInstance[0]));
    }
}