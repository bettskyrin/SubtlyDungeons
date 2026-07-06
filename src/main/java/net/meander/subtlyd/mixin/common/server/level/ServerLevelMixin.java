package net.meander.subtlyd.mixin.common.server.level;

import com.llamalad7.mixinextras.sugar.Local;
import net.meander.subtlyd.world.block.state.BlockStateSD;
import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    private final int MAX_SNOWLOG_LAYERS = BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getPossibleValues().size();

    @Redirect(method = "tickPrecipitation", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z", ordinal = 1))
    private boolean snowlogDuringDownfall(ServerLevel level, BlockPos pos, BlockState state, @Local(name = "maxHeight") int maxHeight) {
        return doSnowlog(level, pos, state, maxHeight);
    }

    @Redirect(method = "tickPrecipitation", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z", ordinal = 2))
    private boolean snowlogDuringDownfall2(ServerLevel level, BlockPos pos, BlockState state, @Local(name = "maxHeight") int maxHeight) {
        return doSnowlog(level, pos, state, maxHeight);
    }

    private boolean doSnowlog(ServerLevel level, BlockPos pos, BlockState state, int maxHeight) {
        BlockState currentTarget = level.getBlockState(pos);
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        if (BlockStateSD.canBeSnowlogged(currentTarget)) {
            int currentLayers = currentTarget.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);
            BlockPos abovePos = pos.above();
            BlockState aboveState = level.getBlockState(abovePos);

            if (currentLayers < MAX_SNOWLOG_LAYERS && currentLayers < maxHeight) {
                return level.setBlockAndUpdate(pos, currentTarget.setValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, currentLayers + 1));
            } else {
                if (aboveState.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS)) {
                    int aboveLayers = aboveState.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);

                    if (aboveLayers < MAX_SNOWLOG_LAYERS && aboveLayers < maxHeight && !aboveState.hasProperty(BlockStatePropertiesSD.BOTTOM_SNOWLOGGED)) {
                        return level.setBlockAndUpdate(abovePos, aboveState.setValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, aboveLayers + 1));
                    }
                } else if (aboveState.isAir() && currentTarget.isFaceSturdy(level, pos, Direction.UP)) {
                    return level.setBlockAndUpdate(abovePos, state);
                }
                return false;
            }
        }

        if (belowState.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS)) {
            int belowLayers = belowState.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);

            if (belowLayers < MAX_SNOWLOG_LAYERS && belowLayers < maxHeight) {
                return level.setBlockAndUpdate(belowPos, belowState.setValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, belowLayers + 1));
            } else {
                if (belowState.isFaceSturdy(level, belowPos, Direction.UP)) {
                    return level.setBlockAndUpdate(pos, state);
                }
                return false;
            }
        }
        return level.setBlockAndUpdate(pos, state);
    }
}