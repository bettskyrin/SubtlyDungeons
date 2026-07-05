package net.meander.subtlyd.mixin.client.options;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.meander.subtlyd.client.OptionsSD;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Options.class)
public class OptionsMixin {
    @Inject(method = "processOptions", at = @At("HEAD"))
    private void saveOptions(Options.FieldAccess access, CallbackInfo ci) {
        access.process("camera_shake", OptionsSD.cameraShake());
        access.process("experimental_gui", OptionsSD.gui());
        access.process("shield_crouch", OptionsSD.shieldCrouch());
        access.process("shield_animation", OptionsSD.shieldAnimation());
    }
}
