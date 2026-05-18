package net.meander.subtlyd.mixin.common.world.level.block;

import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateOffsetMixin {
    /**
     * Grass has an X-Z random offset that makes snowlogged blocks look wrong. This removes that when snowlogged.
     */
    @Inject(method = "getOffset(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/Vec3;", at = @At("HEAD"), cancellable = true)
    private void removeXZOffset(BlockPos pos, CallbackInfoReturnable<Vec3> cir) {
        @SuppressWarnings("DataFlowIssue")
        BlockState state = (BlockState) (Object) this;
        
        if (state.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) && state.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) > 0) {
            cir.setReturnValue(Vec3.ZERO);
        }

        if (state.getBlock() instanceof DoublePlantBlock) {
            if (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
                BlockState belowState = getBlockState(pos.below());

                if (belowState != null && belowState.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) && belowState.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) > 0) {
                    cir.setReturnValue(Vec3.ZERO);
                }
            }
        }
    }

    private static BlockState getBlockState(BlockPos pos) {
        Minecraft minecraft =  Minecraft.getInstance();

        if (minecraft.level != null) {
            return minecraft.level.getBlockState(pos);
        }
        return null;
    }
}