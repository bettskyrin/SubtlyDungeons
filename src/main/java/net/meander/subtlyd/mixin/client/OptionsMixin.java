package net.meander.subtlyd.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class OptionsMixin {
    @Inject(method = "processOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;processDumpedOptions(Lnet/minecraft/client/Options$OptionAccess;)V"))
    private void processOptions(Options.FieldAccess access, CallbackInfo ci) {
        Options options = Minecraft.getInstance().options;

        access.process("camera_shake", options.cameraShake());
        access.process("experimental_gui", options.experimentalGui());
        access.process("shield_crouch", options.shieldCrouch());
        access.process("fancy_entities", options.fancyEntities());
        access.process("shield_animation", options.shieldAnimation());
    }

    @Inject(method = "processDumpedOptions(Lnet/minecraft/client/Options$OptionAccess;)V", at = @At("HEAD"))
    private void processDumpedOptions(Options.OptionAccess access, CallbackInfo ci) {
        Options options = Minecraft.getInstance().options;

        access.process("entity_culling", options.entityCulling());
    }
}
