package net.meander.subtlyd.mixin.common.world.level.block;

import net.meander.subtlyd.sounds.SoundEventsSD;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.sounds.AmbientLeavesBlockSoundPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), name = "ambientLeavesBlockSoundPlayer", argsOnly = true)
    private static AmbientLeavesBlockSoundPlayer modifyLeavesBlockSound(AmbientLeavesBlockSoundPlayer ambientLeavesBlockSoundPlayer) {
        if (ambientLeavesBlockSoundPlayer == AmbientLeavesBlockSoundPlayer.noAmbientSound()) {
            ambientLeavesBlockSoundPlayer = AmbientLeavesBlockSoundPlayer.of(SoundEventsSD.LEAVES_AMBIENT, BlockTags.LOGS);
        }
        return ambientLeavesBlockSoundPlayer;
    }

    @Redirect(method = "animateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/sounds/AmbientLeavesBlockSoundPlayer;playAmbientLeavesSounds(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/util/RandomSource;)V"))
    private void cancelSounds(AmbientLeavesBlockSoundPlayer instance, Level level, BlockPos pos, Block block, RandomSource random) {
        if (!block.defaultBlockState().is(Blocks.PALE_OAK_LEAVES)) {
            instance.playAmbientLeavesSounds(level, pos, block, random);
        }
    }
}
