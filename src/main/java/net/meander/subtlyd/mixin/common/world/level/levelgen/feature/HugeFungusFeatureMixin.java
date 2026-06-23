package net.meander.subtlyd.mixin.common.world.level.levelgen.feature;

import net.meander.subtlyd.world.block.BlocksSD;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.HugeFungusFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HugeFungusFeature.class)
public class HugeFungusFeatureMixin {
    @Shadow @Final private BlockState hatState;

    /**
     * Alters the cap of a huge warped fungus after generation.
     */
    @Inject(method = "place", at = @At("RETURN"))
    private void alterWarpedFungusHat(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            if (hatState.is(Blocks.WARPED_WART_BLOCK)) {
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
