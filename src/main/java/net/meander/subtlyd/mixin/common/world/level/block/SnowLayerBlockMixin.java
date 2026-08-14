package net.meander.subtlyd.mixin.common.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowLayerBlock.class)
public class SnowLayerBlockMixin {
    @Inject(method = "canBeReplaced", at = @At("HEAD"), cancellable = true)
    private void allowSnowloggablesToReplaceSnow(BlockState state, BlockPlaceContext context, CallbackInfoReturnable<Boolean> cir) {
        final int maxLayers = BlockStateProperties.SNOWLOGGED_LAYERS.getPossibleValues().getLast();
        int layers = state.getValue(SnowLayerBlock.LAYERS);
        
        if (layers < maxLayers && context.getItemInHand().getItem() instanceof BlockItem blockItem) {
            if (blockItem.getBlock().defaultBlockState().hasProperty(BlockStateProperties.SNOWLOGGED_LAYERS)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void allowSnowlayersOnSnowloggables(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState belowState = level.getBlockState(pos.below());

        if (belowState.hasProperty(BlockStateProperties.SNOWLOGGED_LAYERS) && belowState.getValue(BlockStateProperties.SNOWLOGGED_LAYERS) == 8) {
            cir.setReturnValue(true);
        }
    }
}