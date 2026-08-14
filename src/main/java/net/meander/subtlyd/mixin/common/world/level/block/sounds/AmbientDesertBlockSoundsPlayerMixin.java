package net.meander.subtlyd.mixin.common.world.level.block.sounds;

import net.minecraft.world.level.block.sounds.AmbientDesertBlockSoundsPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AmbientDesertBlockSoundsPlayer.class)
public class AmbientDesertBlockSoundsPlayerMixin {
    @ModifyArg(method = {"playAmbientSandSounds", "playAmbientDeadBushSounds"},
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"), index = 5)
    private static float increaseVolume(float volume) {
        return 2.0F;
    }

    @ModifyArg(method = "playAmbientDryGrassSounds", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playPlayerSound(Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"), index = 2)
    private static float increasePlayerSoundVolume(float volume) {
        return 2.0F;
    }
}
