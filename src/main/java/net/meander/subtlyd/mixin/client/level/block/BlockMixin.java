package net.meander.subtlyd.mixin.client.level.block;

import net.meander.subtlyd.tags.BlockTagsSD;
import net.meander.subtlyd.world.level.block.sounds.AmbientAirBlockSoundsPlayer;
import net.meander.subtlyd.world.level.block.sounds.AmbientBushBlockSoundsPlayer;
import net.meander.subtlyd.world.level.block.sounds.AmbientGrassyBlockSoundsPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "animateTick", at = @At("HEAD"))
    private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        playAmbientSounds(state, level, pos, random);
    }

    /**
     * Plays block based ambient sounds.
     * @param state The sound playing blockstate.
     * @param level The world/level.
     * @param pos The block position to play the sound at.
     * @param random A randomSource type to determine the likelihood of sounds playing.
     */
    private void playAmbientSounds(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.is(BlockTagsSD.TRIGGERS_AMBIENT_WIND_BLOCK_SOUNDS)) {
            AmbientAirBlockSoundsPlayer.playColdWindSounds(level, pos, random);
        }

        if (state.is(BlockTagsSD.TRIGGERS_AMBIENT_BUSH_BLOCK_SOUNDS)) {
            AmbientBushBlockSoundsPlayer.playAmbientBushSounds(level, pos, random);
        }

        if (state.is(BlockTagsSD.TRIGGERS_AMBIENT_GRASS_BLOCK_SOUNDS)) {
            AmbientGrassyBlockSoundsPlayer.playAmbientGrassSounds(level, pos, random);
        }
    }
}
