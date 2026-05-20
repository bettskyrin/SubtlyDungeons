package net.meander.subtlyd.mixin.common.world.level.levelgen.feature;

import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.SnowAndFreezeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SnowAndFreezeFeature.class)
public class SnowAndFreezeFeatureMixin {
    @Redirect(method = "place", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/WorldGenLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private boolean snowlogDuringWorldGen(WorldGenLevel level, BlockPos pos, BlockState state, int flags) {
        if (state.is(Blocks.SNOW)) {
            final int MAX_LAYERS = BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getPossibleValues().getLast();
            BlockState currentTarget = level.getBlockState(pos);
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);

            if (currentTarget.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) && state.getBlock().defaultBlockState().canSurvive(level, pos)) {
                int currentLayers = currentTarget.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);

                if (currentLayers < MAX_LAYERS) {
                    return level.setBlock(pos, currentTarget.setValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, currentLayers + 1), flags);
                } else {
                    BlockPos abovePos = pos.above();
                    BlockState aboveState = level.getBlockState(abovePos);

                    if (aboveState.isAir() || aboveState.canBeReplaced()) {
                        return level.setBlock(abovePos, state, flags);
                    }
                    return false;
                }
            }

            if (belowState.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS)) {
                int belowLayers = belowState.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);

                if (belowLayers < MAX_LAYERS) {
                    return level.setBlock(belowPos, belowState.setValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, belowLayers + 1), flags);
                } else {
                    return level.setBlock(pos, state, flags);
                }
            }
        }
        return level.setBlock(pos, state, flags);
    }
}