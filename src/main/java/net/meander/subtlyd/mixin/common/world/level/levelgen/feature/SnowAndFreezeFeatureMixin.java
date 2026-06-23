package net.meander.subtlyd.mixin.common.world.level.levelgen.feature;

import net.meander.subtlyd.world.block.state.BlockStateSD;
import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.SnowAndFreezeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowAndFreezeFeature.class)
public class SnowAndFreezeFeatureMixin {
    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void placeDriplineSnow(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin, CallbackInfoReturnable<Boolean> cir) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        boolean wasSnowPlaced = false;

        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                int x = origin.getX() + dx;
                int z = origin.getZ() + dz;

                int groundY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                pos.set(x, groundY, z);

                if (tryPlaceSnowAt(level, pos)) {
                    wasSnowPlaced = true;
                }

                int canopyY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);

                if (canopyY > groundY) {
                    pos.set(x, canopyY, z);
                    if (tryPlaceSnowAt(level, pos)) {
                        wasSnowPlaced = true;
                    }
                }
            }
        }

        if (wasSnowPlaced) {
            cir.setReturnValue(true);
        }
    }

    private boolean tryPlaceSnowAt(WorldGenLevel level, BlockPos pos) {
        Biome biome = level.getBiome(pos).value();

        if (biome.shouldSnow(level, pos)) {
            BlockState state = level.getBlockState(pos);

            if (BlockStateSD.canBeSnowlogged(state)) {
                BlockState belowState = level.getBlockState(pos.below());

                level.setBlock(pos, state.setValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, 1), 2);

                if (belowState.hasProperty(BlockStateProperties.SNOWY)) {
                    level.setBlock(pos.below(), belowState.setValue(BlockStateProperties.SNOWY, true), 2);
                }

                if (state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF) && state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
                    BlockPos abovePos = pos.above();
                    BlockState aboveState = level.getBlockState(abovePos);

                    if (aboveState.hasProperty(BlockStatePropertiesSD.BOTTOM_SNOWLOGGED)) {
                        level.setBlock(abovePos, aboveState.setValue(BlockStatePropertiesSD.BOTTOM_SNOWLOGGED, true), 2);
                    }
                }
                return true;
            } else if (state.isAir()) {
                BlockState belowState = level.getBlockState(pos.below());

                level.setBlock(pos, Blocks.SNOW.defaultBlockState(), 3);

                if (belowState.hasProperty(SnowyBlock.SNOWY)) {
                    level.setBlock(pos.below(), belowState.setValue(SnowyBlock.SNOWY, true), 3);
                }
                return true;
            }
        }
        return false;
    }
}