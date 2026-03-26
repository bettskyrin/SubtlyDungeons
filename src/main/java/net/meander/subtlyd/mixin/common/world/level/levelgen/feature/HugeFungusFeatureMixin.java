package net.meander.subtlyd.mixin.common.world.level.levelgen.feature;

import net.meander.subtlyd.world.block.BlocksSD;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
import net.minecraft.world.level.levelgen.feature.HugeFungusFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HugeFungusFeature.class)
public class HugeFungusFeatureMixin {
    @Inject(method = "placeHatBlock", at = @At("TAIL"))
    private void placeHatBlock(LevelAccessor level, RandomSource random, HugeFungusConfiguration config, BlockPos.MutableBlockPos blockPos, float decorBlockProbability, float hatBlockProbability, float vinesProbability, CallbackInfo ci) {
        alterWarpedFungusHat(level, config.hatState, blockPos, random);
    }

    @Inject(method = "placeHatDropBlock", at = @At("TAIL"))
    private void placeHatDropBlock(LevelAccessor level, RandomSource random, BlockPos blockPos, BlockState hatState, boolean placeVines, CallbackInfo ci) {
        alterWarpedFungusHat(level, hatState, blockPos, random);
    }

    /**
     * Alters the cap of a huge warped fungus.
     * @param level The game level.
     * @param hatState The potential blockstate of the block being placed.
     * @param blockPos The block position of the block being placed.
     * @param random A randomSource.
     */
    private void alterWarpedFungusHat(LevelAccessor level, BlockState hatState, BlockPos blockPos, RandomSource random) {
        if (hatState.is(Blocks.WARPED_WART_BLOCK)) {
            if (level.getBlockState(blockPos).is(Blocks.WARPED_WART_BLOCK)) {

                if (level.getBlockState(blockPos.below()).isAir()) {
                    level.setBlock(blockPos.below(), BlocksSD.WARPED_OVERHANG.defaultBlockState(), 3);
                }

                if (level.getBlockState(blockPos.above()).isAir() && level.getBlockState(blockPos.above(2)).isAir() && random.nextFloat() <= 0.1) {
                    level.setBlock(blockPos.above(), Blocks.WARPED_ROOTS.defaultBlockState(), 3);
                }
            }
        }
    }
}
