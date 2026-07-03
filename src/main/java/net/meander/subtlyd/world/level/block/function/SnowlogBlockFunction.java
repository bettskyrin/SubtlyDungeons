package net.meander.subtlyd.world.level.block.function;

import net.fabricmc.fabric.api.event.player.BlockEvents;
import net.meander.subtlyd.world.level.block.SimpleSnowloggedBlock;
import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SnowlogBlockFunction implements BlockEvents.UseItemOnCallback {
    @Override
    public InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (blockState.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS)) {
            SimpleSnowloggedBlock snowloggedBlock = (SimpleSnowloggedBlock) blockState.getBlock();
            InteractionResult result = snowloggedBlock.trySnowlog(blockState, level, blockPos, player, interactionHand);

            if (result.consumesAction()) {
                return result;
            }
        }
        return InteractionResult.PASS;
    }
}
