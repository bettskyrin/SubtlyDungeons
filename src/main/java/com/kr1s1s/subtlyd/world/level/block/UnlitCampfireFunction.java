package com.kr1s1s.subtlyd.world.level.block;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class UnlitCampfireFunction implements UseBlockCallback {
    @Override
    public InteractionResult interact(Player player, Level level, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockState blockState = level.getBlockState(blockPos);
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (blockState.getBlock() instanceof CampfireBlock && !blockState.getValue(CampfireBlock.LIT) && itemStack.getItem() == Items.STICK) {
            if (level.getRandom().nextFloat() > 0.7F) {
                level.setBlock(blockPos, blockState.setValue(CampfireBlock.LIT, true), 3);

            }
            if (!player.gameMode().isCreative()) {
                itemStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
