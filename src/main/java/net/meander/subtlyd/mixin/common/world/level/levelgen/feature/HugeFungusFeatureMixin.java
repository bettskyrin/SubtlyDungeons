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
        placeWarpedOverhang(level, config.hatState, blockPos);
    }

    @Inject(method = "placeHatDropBlock", at = @At("TAIL"))
    private void placeHatDropBlock(LevelAccessor level, RandomSource random, BlockPos blockPos, BlockState hatState, boolean placeVines, CallbackInfo ci) {
        placeWarpedOverhang(level, hatState, blockPos);
    }

    private void placeWarpedOverhang(LevelAccessor level, BlockState hatState, BlockPos blockPos) {
        if (hatState.is(Blocks.WARPED_WART_BLOCK)) {
            BlockPos downPos = blockPos.below();

            if (level.getBlockState(downPos).isAir() && level.getBlockState(blockPos).is(Blocks.WARPED_WART_BLOCK)) {
                level.setBlock(downPos, BlocksSD.WARPED_WART_OVERHANG.defaultBlockState(), 3);
            }
        }
    }
}
