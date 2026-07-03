package net.meander.subtlyd.world.level.block.function;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.meander.subtlyd.core.cauldron.CauldronInteractionsSD;
import net.meander.subtlyd.world.block.BlocksSD;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;

public class PotionCauldronFunction implements UseBlockCallback {
    @Override
    public InteractionResult interact(@NonNull Player player, @NonNull Level level, InteractionHand interactionHand, @NonNull BlockHitResult blockHitResult) {
        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockState blockState = level.getBlockState(blockPos);
        ItemStack itemStack = player.getItemInHand(interactionHand);
        InteractionResult result = InteractionResult.PASS;

        if (itemStack.is(Items.POTION) || itemStack.is(Items.SPLASH_POTION) || itemStack.is(Items.LINGERING_POTION)) {
            if (blockState.is(Blocks.CAULDRON)) {
                result = CauldronInteractionsSD.fillEmptyCauldronWithPotion(blockState, level, blockPos, player, interactionHand, itemStack);
            } else if (blockState.is(BlocksSD.POTION_CAULDRON)) {
                result = CauldronInteractionsSD.fillPotionCauldronWithPotion(blockState, level, blockPos, player, interactionHand, itemStack);
            }
        } else if (itemStack.is(Items.GLASS_BOTTLE)) {
            result = CauldronInteractionsSD.fillBottle(blockState, level, blockPos, player, interactionHand, itemStack);
        } else if (itemStack.is(Items.ARROW)) {
            result = CauldronInteractionsSD.createTippedArrow(blockState, level, blockPos, player, interactionHand, itemStack);
        }
        return result;
    }
}
