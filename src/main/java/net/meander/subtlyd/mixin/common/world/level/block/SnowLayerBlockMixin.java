package net.meander.subtlyd.mixin.common.world.level.block;

import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowLayerBlock.class)
public class SnowLayerBlockMixin {
    /**
     * Allows for snow to be replaced by snowloggable blocks
     * @param state The snow block
     */
    @Inject(method = "canBeReplaced", at = @At("HEAD"), cancellable = true)
    private void allowSnowloggablesToReplaceSnow(BlockState state, BlockPlaceContext context, CallbackInfoReturnable<Boolean> cir) {
        int layers = state.getValue(SnowLayerBlock.LAYERS);
        int maxLayers = BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getPossibleValues().getLast();
        
        if (layers < maxLayers && context.getItemInHand().getItem() instanceof BlockItem snowloggable) {
            if (snowloggable.getBlock().defaultBlockState().hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS)) {
                cir.setReturnValue(true);
            }
        }
    }
}