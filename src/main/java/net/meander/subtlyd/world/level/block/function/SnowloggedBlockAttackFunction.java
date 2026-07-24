package net.meander.subtlyd.world.level.block.function;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SnowloggedBlockAttackFunction implements AttackBlockCallback {
    @Override
    public InteractionResult interact(Player player, Level level, InteractionHand interactionHand, BlockPos blockPos, Direction direction) {
        BlockState blockState = level.getBlockState(blockPos);

        if (blockState.hasProperty(BlockStateProperties.SNOWLOGGED_LAYERS)) {
            int layers = blockState.getValue(BlockStateProperties.SNOWLOGGED_LAYERS);

            if (layers > 0) {
                if (level instanceof ServerLevel) {
                    ItemStack tool = player.getItemInHand(interactionHand);

                    if (tool.is(ItemTags.SHOVELS) && !player.hasInfiniteMaterials()) {
                        Block.popResource(level, blockPos, new ItemStack(Items.SNOWBALL, layers));
                        tool.hurtAndBreak(1, player, player.getEquipmentSlotForItem(tool));
                    }

                    level.setBlock(blockPos, blockState.setValue(BlockStateProperties.SNOWLOGGED_LAYERS, 0), 3);
                    level.playSound(null, blockPos, SoundEvents.SNOW_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
