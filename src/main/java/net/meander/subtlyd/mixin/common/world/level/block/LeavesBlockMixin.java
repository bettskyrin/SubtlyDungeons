package net.meander.subtlyd.mixin.common.world.level.block;

import net.meander.subtlyd.sounds.SoundEventsSD;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.sounds.AmbientLeavesBlockSoundPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), name = "ambientLeavesBlockSoundPlayer", argsOnly = true)
    private static AmbientLeavesBlockSoundPlayer modifyLeavesBlockSound(AmbientLeavesBlockSoundPlayer ambientLeavesBlockSoundPlayer) {
        if (ambientLeavesBlockSoundPlayer == AmbientLeavesBlockSoundPlayer.noAmbientSound()) {
            ambientLeavesBlockSoundPlayer = AmbientLeavesBlockSoundPlayer.of(SoundEventsSD.LEAVES_AMBIENT, BlockTags.LOGS);
        }
        return ambientLeavesBlockSoundPlayer;
    }
}
