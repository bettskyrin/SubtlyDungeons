package net.meander.subtlyd.mixin.client.resources.sounds;

import com.llamalad7.mixinextras.sugar.Local;
import net.meander.subtlyd.sounds.SoundEventsSD;
import net.minecraft.client.resources.sounds.GuardianAttackSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Guardian;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuardianAttackSoundInstance.class)
public class GuardianAttackSoundInstanceMixin {
    @ModifyArg(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/resources/sounds/AbstractTickableSoundInstance;<init>(Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;Lnet/minecraft/util/RandomSource;)V"
        ),
        index = 0
    )
    private static SoundEvent setElderChargeSound(SoundEvent event, @Local(argsOnly = true, name = "guardian") Guardian guardian) {
        if (guardian instanceof ElderGuardian) {
            return SoundEventsSD.ELDER_GUARDIAN_ATTACK;
        }

        return event;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void modifyAttenuation(CallbackInfo ci) {
        GuardianAttackSoundInstance soundInstance = (GuardianAttackSoundInstance) (Object) this;
        soundInstance.attenuation = SoundInstance.Attenuation.LINEAR;
    }
}