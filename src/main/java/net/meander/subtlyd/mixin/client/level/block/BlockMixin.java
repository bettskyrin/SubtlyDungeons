package net.meander.subtlyd.mixin.client.level.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.meander.subtlyd.data.tags.BlockTagsSD;
import net.meander.subtlyd.world.level.block.sounds.AmbientAirBlockSoundsPlayer;
import net.meander.subtlyd.world.level.block.sounds.AmbientBushBlockSoundsPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "animateTick", at = @At("HEAD"))
    private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        playAmbientSounds(state.getBlock(), level, pos, random);
    }

    /**
     * Plays block based ambient sounds.
     * @param block The sound playing block.
     * @param level The world/level.
     * @param blockPos The block position to play the sound at.
     * @param randomSource A randomSource type to determine the likelihood of sounds playing.
     */
    private void playAmbientSounds(Block block, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (block.defaultBlockState().is(BlockTagsSD.TRIGGERS_AMBIENT_WIND_BLOCK_SOUNDS)) {
            AmbientAirBlockSoundsPlayer.playColdWindSounds(level, blockPos, randomSource);
        }

        if (block.defaultBlockState().is(BlockTagsSD.TRIGGERS_AMBIENT_BUSH_BLOCK_SOUNDS)) {
            AmbientBushBlockSoundsPlayer.playAmbientBushSounds(level, blockPos, randomSource);
        }
    }
}
