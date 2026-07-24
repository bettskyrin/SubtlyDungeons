package net.meander.subtlyd.mixin.common.world.level.block;

import net.minecraft.world.level.block.SnowyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowyBlock.class)
public class SnowyBlockMixin {
    @Inject(method = "isSnowySetting", at = @At("RETURN"), cancellable = true)
    private static void setSnowyWhenSnowlogged(BlockState aboveState, CallbackInfoReturnable<Boolean> cir) {
        if (aboveState.hasProperty(BlockStateProperties.SNOWLOGGED_LAYERS) && aboveState.getValue(BlockStateProperties.SNOWLOGGED_LAYERS) > 0) {
            cir.setReturnValue(true);
        }
    }
}
