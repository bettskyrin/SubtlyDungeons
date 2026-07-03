package net.meander.subtlyd.mixin.common.world.level.block.state;

import net.meander.subtlyd.world.level.block.SimpleSnowloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {
    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void trySnowlog(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (state.getBlock() instanceof SimpleSnowloggedBlock snowloggedBlock) {
            InteractionResult result = snowloggedBlock.trySnowlog(state, level, pos, player, hand);
            
            if (result.consumesAction()) {
                cir.setReturnValue(result);
            }
        }
    }
}