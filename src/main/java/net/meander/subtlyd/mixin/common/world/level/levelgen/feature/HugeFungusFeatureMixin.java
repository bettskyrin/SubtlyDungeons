package net.meander.subtlyd.mixin.common.world.level.levelgen.feature;

import net.meander.subtlyd.world.block.BlocksSD;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
import net.minecraft.world.level.levelgen.feature.HugeFungusFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HugeFungusFeature.class)
public class HugeFungusFeatureMixin {
//    @Inject(method = "placeHatBlock", at = @At("TAIL"))
//    private void placeHatBlock(LevelAccessor level, RandomSource random, HugeFungusConfiguration config, BlockPos.MutableBlockPos blockPos, float decorBlockProbability, float hatBlockProbability, float vinesProbability, CallbackInfo ci) {
//        alterWarpedFungusHat(level, config.hatState, blockPos, random, decorBlockProbability);
//    }
//
//    @Inject(method = "placeHatDropBlock", at = @At("TAIL"))
//    private void placeHatDropBlock(LevelAccessor level, RandomSource random, BlockPos blockPos, BlockState hatState, boolean placeVines, CallbackInfo ci) {
//        alterWarpedFungusHat(level, hatState, blockPos, random, null);
//    }
//
//    /**
//     * Alters the cap of a huge warped fungus.
//     * @param level The game level.
//     * @param hatState The potential blockstate of the block being placed.
//     * @param blockPos The block position of the block being placed.
//     * @param random A randomSource.
//     */
//    private void alterWarpedFungusHat(final LevelAccessor level, final BlockState hatState, final BlockPos blockPos, final RandomSource random, final @Nullable Float decorBlockProbability) {
//        if (hatState.is(Blocks.WARPED_WART_BLOCK)) {
//            BlockPos.MutableBlockPos overhangPos = blockPos.mutable().move(Direction.DOWN);
//            BlockPos.MutableBlockPos rootsPos = blockPos.mutable().move(Direction.UP);
//
//            if (level.isEmptyBlock(overhangPos) && !level.getBlockState(blockPos.below()).is(hatState.getBlock())) {
//                level.setBlock(blockPos.below(), BlocksSD.WARPED_OVERHANG.defaultBlockState(), 3);
//            }
//
//            if (level.isEmptyBlock(rootsPos) && decorBlockProbability != null && decorBlockProbability <= 0.1) {
//                level.setBlock(rootsPos, Blocks.WARPED_ROOTS.defaultBlockState(), 3);
//            }
//        }
//    }

    /**
     * Alters the cap of a huge warped fungus after generation.
     */
    @Inject(method = "place", at = @At("RETURN"))
    private void alterWarpedFungusHat(FeaturePlaceContext<HugeFungusConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            HugeFungusConfiguration config = context.config();

            if (config.hatState.is(Blocks.WARPED_WART_BLOCK)) {
                LevelAccessor level = context.level();
                BlockPos origin = context.origin();
                RandomSource random = context.random();

                BlockPos minPos = origin.offset(-4, 0, -4);
                BlockPos maxPos = origin.offset(4, 16, 4);

                for (BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
                    if (level.getBlockState(pos).is(Blocks.WARPED_WART_BLOCK)) {

                        BlockPos below = pos.below();
                        BlockPos above = pos.above();

                        if (level.isEmptyBlock(below)) {
                            level.setBlock(below, BlocksSD.WARPED_OVERHANG.defaultBlockState(), 3);
                        }

                        if (level.isEmptyBlock(above) && random.nextFloat() <= 0.1F) {
                            level.setBlock(above, Blocks.WARPED_ROOTS.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
    }
}
