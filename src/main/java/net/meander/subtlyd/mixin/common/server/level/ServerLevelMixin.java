package net.meander.subtlyd.mixin.common.server.level;

import net.meander.subtlyd.data.models.blockstates.SnowloggableBlocks;
import net.meander.subtlyd.server.ServerLevelSD;
import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Redirect(method = "tickPrecipitation", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"))
    private boolean snowlogDuringDownfall(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.is(Blocks.SNOW)) {
            final int MAX_LAYERS = SnowloggableBlocks.MAX_LAYERS;
            BlockState currentTarget = level.getBlockState(pos);
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);

            if (currentTarget.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) && state.getBlock().defaultBlockState().canSurvive(level, belowPos)) {
                int currentLayers = currentTarget.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);

                if (currentLayers < MAX_LAYERS) {
                    return level.setBlockAndUpdate(pos, currentTarget.setValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, currentLayers + 1));
                } else {
                    BlockPos abovePos = pos.above();
                    BlockState aboveState = level.getBlockState(abovePos);

                    if (aboveState.isAir() || aboveState.canBeReplaced()) {
                        return level.setBlockAndUpdate(abovePos, state);
                    }
                    return false;
                }
            }

            if (belowState.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS)) {
                int belowLayers = belowState.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);

                if (belowLayers < MAX_LAYERS) {
                    return level.setBlockAndUpdate(belowPos, belowState.setValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, belowLayers + 1));
                } else {
                    return level.setBlockAndUpdate(pos, state);
                }
            }
        }
        return level.setBlockAndUpdate(pos, state);
    }


    /**
     * Decreases the time in that weather stays clear.
     */
    @ModifyVariable(method = "advanceWeatherCycle", at = @At(value = "LOAD", ordinal = 0), name = "clearWeatherTime")
    private int decreaseClearWeatherTime(int clearWeatherTime) {
        ServerLevel level = (ServerLevel) (Object) this;

        if (!level.isRaining()) {
            float clockRate = Mth.ceil(level.clockManager().packState().clocks().get(level.dimensionType().defaultClock().get()).rate());

            if (((clockRate * level.getDefaultClockTime()) % 240) == 0) {
                int adjustment = ServerLevelSD.getDownfallModifier(level);

                return Math.max(0, clearWeatherTime - (adjustment * 100));
            }
        }
        return clearWeatherTime;
    }

    /**
     * Decreases the time in between downfall
     */
    @ModifyVariable(method = "advanceWeatherCycle", at = @At(value = "LOAD", ordinal = 0), name = "rainTime")
    private int increaseBiomeDownfall(int rainTime) {
        ServerLevel level = (ServerLevel) (Object) this;

        if (!level.isRaining()) {
            float clockRate = Mth.ceil(level.clockManager().packState().clocks().get(level.dimensionType().defaultClock().get()).rate());

            if (((clockRate * level.getDefaultClockTime()) % 240) == 0) {
                int adjustment = ServerLevelSD.getDownfallModifier(level);

                return Math.max(1, (rainTime) - adjustment);
            }
        }
        return rainTime;
    }
}