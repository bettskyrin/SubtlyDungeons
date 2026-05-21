package net.meander.subtlyd.world.level.block;

import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public interface SimpleSnowloggedBlock {
    default InteractionResult trySnowlog(BlockState state, LevelAccessor level, BlockPos pos, Player player, InteractionHand hand) {
        if (SimpleSnowloggedBlock.isSnowloggable(state.getBlock())) {
            final int MAX_LAYERS = BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getPossibleValues().getLast();
            int layers = state.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);
            ItemStack heldItem = player.getItemInHand(hand);
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);

            if (heldItem.is(Items.SNOW)) {
                if (state.getBlock() instanceof DoublePlantBlock) {
                    if (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
                        if (SimpleSnowloggedBlock.isSnowloggable(belowState.getBlock()) && belowState.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) < 8) {
                            pos = belowPos;
                            state = belowState;
                        }
                    } else if (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
                        int lowerLayers = state.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);

                        if (lowerLayers == 8) {
                            BlockPos abovePos = pos.above();
                            BlockState aboveState = level.getBlockState(abovePos);

                            if (SimpleSnowloggedBlock.isSnowloggable(aboveState.getBlock())) {
                                pos = abovePos;
                                state = aboveState;
                            }
                        }
                    }
                }

                if (layers < MAX_LAYERS) {
                    if (!level.isClientSide()) {
                        level.setBlock(pos, state.setValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, layers == 0 ? 1 : layers + 1), 3);
                        level.playSound(null, pos, SoundEvents.SNOW_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

                        if (!player.hasInfiniteMaterials()) {
                            heldItem.shrink(1);
                        }
                    }
                }
                return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    static boolean isSnowloggable(Block block) {
        return block instanceof SegmentableBlock || block instanceof VegetationBlock || block instanceof CrossCollisionBlock ||
                block instanceof FenceGateBlock || block instanceof WallBlock;
    }
}
