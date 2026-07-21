package net.meander.subtlyd.world.level.block.function;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.meander.subtlyd.sounds.SoundEventsSD;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;

public class UnlitCampfireFunction implements UseBlockCallback {
    @Override
    public InteractionResult interact(@NonNull Player player, @NonNull Level level, InteractionHand interactionHand, @NonNull BlockHitResult blockHitResult) {
        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockState blockState = level.getBlockState(blockPos);
        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (blockState.getBlock() instanceof CampfireBlock && !blockState.getValue(CampfireBlock.LIT) && itemStack.getItem() == Items.STICK) {
            itemStack.consume(1, player);
            level.playSound(null, blockPos, SoundEventsSD.STICK_LIGHT, SoundSource.BLOCKS);

            if (level instanceof ServerLevel) {
                if (level.getRandom().nextFloat() > 0.7F) {
                    level.setBlock(blockPos, blockState.setValue(CampfireBlock.LIT, true), 3);
                }

                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemStack);
                }

                return InteractionResult.SUCCESS_SERVER;
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
