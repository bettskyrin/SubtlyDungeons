package net.meander.subtlyd.mixin.client.options;

import net.meander.subtlyd.client.OptionInstanceSD;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class OptionsMixin {
    /**
     * Saves the new custom options
     */
    @Inject(method = "processOptions", at = @At("HEAD"))
    private void saveOptions(Options.FieldAccess access, CallbackInfo ci) {
        access.process("camera_shake", OptionInstanceSD.CAMERA_SHAKE);
        access.process("experimental_ui", OptionInstanceSD.EXPERIMENTAL_UI);
    }
}
