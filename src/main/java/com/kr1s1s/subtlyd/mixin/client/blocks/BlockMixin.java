package com.kr1s1s.subtlyd.mixin.client.blocks;

import com.kr1s1s.subtlyd.world.level.block.sounds.AmbientAirBlockSoundsPlayer;
import com.kr1s1s.subtlyd.world.level.block.sounds.AmbientBushBlockSoundsPlayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "animateTick", at = @At("HEAD"))
    private void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
        playAmbientSounds(blockState.getBlock(), level, blockPos, randomSource);
    }

    /**
     * Plays block based ambient sounds.
     * @param block The sound playing block.
     * @param level The world/level.
     * @param blockPos The block position to play the sound at.
     * @param randomSource A randomSource type to determine the likelihood of sounds playing.
     */
    private void playAmbientSounds(Block block, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (block instanceof AirBlock) {
            AmbientAirBlockSoundsPlayer.playColdWindSounds(level, blockPos, randomSource);
        } else if (block instanceof BushBlock) {
            AmbientBushBlockSoundsPlayer.playAmbientBushSounds(level, blockPos, randomSource);
        }
    }
}
