package net.meander.subtlyd.mixin.client.options;

import net.meander.subtlyd.client.OptionsSD;
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
        access.process("camera_shake", OptionsSD.CAMERA_SHAKE);
        access.process("experimental_gui", OptionsSD.EXPERIMENTAL_GUI);
        access.process("shield_crouch", OptionsSD.SHIELD_CROUCH);
    }
}
