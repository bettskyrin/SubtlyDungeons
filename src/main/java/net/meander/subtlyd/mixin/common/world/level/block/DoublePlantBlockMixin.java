package net.meander.subtlyd.mixin.common.world.level.block;

import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DoublePlantBlock.class)
public class DoublePlantBlockMixin {
    @Inject(method = "updateShape", at = @At("RETURN"), cancellable = true)
    private void syncSnowloggedState(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random, CallbackInfoReturnable<BlockState> cir) {
        if (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER && directionToNeighbour == Direction.DOWN) {
            BlockState resultState = cir.getReturnValue();
            
            if (resultState.hasProperty(BlockStatePropertiesSD.BOTTOM_SNOWLOGGED)) {
                boolean isBottomSnowlogged = neighbourState.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) > 0;
                
                cir.setReturnValue(resultState.setValue(BlockStatePropertiesSD.BOTTOM_SNOWLOGGED, isBottomSnowlogged));
            }
        }
    }
}