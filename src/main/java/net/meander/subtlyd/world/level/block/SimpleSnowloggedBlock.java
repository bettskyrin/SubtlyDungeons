package net.meander.subtlyd.world.level.block;

import net.meander.subtlyd.world.block.state.BlockStateSD;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

/**
 * @see SimpleWaterloggedBlock
 */
public interface SimpleSnowloggedBlock {
    default InteractionResult trySnowlog(BlockState state, LevelAccessor level, BlockPos pos, Player player, InteractionHand hand) {
        if (BlockStateSD.canBeSnowlogged(state)) {
            final int MAX_LAYERS = BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getPossibleValues().getLast();
            int layers = state.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);
            ItemStack heldItem = player.getItemInHand(hand);
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);

            if (heldItem.is(Items.SNOW)) {
                if (state.getBlock() instanceof DoublePlantBlock) {
                    if (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
                        if (belowState.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) && belowState.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) < 8) {
                            pos = belowPos;
                            state = belowState;
                            layers = state.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);
                        }
                    } else if (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
                        if (layers == 8) {
                            BlockPos abovePos = pos.above();
                            BlockState aboveState = level.getBlockState(abovePos);

                            if (aboveState.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS)) {
                                pos = abovePos;
                                state = aboveState;
                                layers = state.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);
                            }
                        }
                    }
                }

                if (layers < MAX_LAYERS) {
                    if (!level.isClientSide()) {
                        BlockState snowloggedState = state.setValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, layers == 0 ? 1 : layers + 1);

                        if (snowloggedState.hasProperty(BlockStateProperties.SNOWY)) {
                            snowloggedState = snowloggedState.setValue(BlockStateProperties.SNOWY, true);
                        }

                        level.setBlock(pos, snowloggedState, 3);
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
}
