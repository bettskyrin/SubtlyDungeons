package net.meander.subtlyd.mixin.common.world.level.levelgen.feature;

import net.meander.subtlyd.world.level.block.SimpleSnowloggedBlock;
import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.SnowAndFreezeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

            if (SimpleSnowloggedBlock.isSnowloggable(currentTarget.getBlock())) {
                if (state.getBlock().defaultBlockState().canSurvive(level, pos)) {
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
            }

            if (SimpleSnowloggedBlock.isSnowloggable(belowState.getBlock())) {
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

    @Inject(method = "place", at = @At("TAIL"))
    private void placeDriplineSnow(FeaturePlaceContext<NoneFeatureConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        BlockState snowState = Blocks.SNOW.defaultBlockState();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int blockX = origin.getX() + x;
                int blockZ = origin.getZ() + z;

                int canopyY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, blockX, blockZ);
                int groundY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockX, blockZ);

                if (canopyY > groundY) {
                    BlockPos surfacePos = new BlockPos(blockX, groundY, blockZ);
                    BlockState surfaceState = level.getBlockState(surfacePos);

                    if (level.getBiome(surfacePos).value().coldEnoughToSnow(surfacePos, level.getSeaLevel())) {
                        boolean canPlace = SimpleSnowloggedBlock.isSnowloggable(surfaceState.getBlock())
                                || (surfaceState.canBeReplaced() && snowState.canSurvive(level, surfacePos));

                        if (canPlace) {
                            if (snowlogDuringWorldGen(level, surfacePos, snowState, 2)) {
                                BlockPos groundPos = surfacePos.below();
                                BlockState groundState = level.getBlockState(groundPos);

                                if (groundState.hasProperty(BlockStateProperties.SNOWY)) {
                                    level.setBlock(groundPos, groundState.setValue(BlockStateProperties.SNOWY, true), 2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}