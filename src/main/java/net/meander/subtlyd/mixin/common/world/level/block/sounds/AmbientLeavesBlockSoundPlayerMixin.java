package net.meander.subtlyd.mixin.common.world.level.block.sounds;

import net.minecraft.world.level.block.sounds.AmbientLeavesBlockSoundPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AmbientLeavesBlockSoundPlayer.class)
public class AmbientLeavesBlockSoundPlayerMixin {
    @ModifyArg(method = "playAmbientLeavesSounds",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"), index = 5)
    private static float increaseVolume(float volume) {
        return 2.0F;
    }
}
