package net.meander.subtlyd.mixin.common.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DoublePlantBlock.class)
public class DoublePlantBlockMixin {
    @Inject(method = "updateShape", at = @At("RETURN"), cancellable = true)
    private void syncSnowloggedState(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random, CallbackInfoReturnable<BlockState> cir) {
        if (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER && directionToNeighbour == Direction.DOWN) {
            BlockState resultState = cir.getReturnValue();
            
            if (resultState.hasProperty(BlockStateProperties.BOTTOM_SNOWLOGGED)) {
                boolean isBottomSnowlogged = neighbourState.getValue(BlockStateProperties.SNOWLOGGED_LAYERS) > 0;
                
                cir.setReturnValue(resultState.setValue(BlockStateProperties.BOTTOM_SNOWLOGGED, isBottomSnowlogged));
            }
        }
    }

    @Inject(method = "setPlacedBy", at = @At("RETURN"))
    private void syncSnowloggedStateWhenPlaced(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack, CallbackInfo ci) {
        BlockPos upperPos = pos.above();
        BlockState upperState = level.getBlockState(upperPos);

        if (upperState.hasProperty(BlockStateProperties.BOTTOM_SNOWLOGGED) && state.hasProperty(BlockStateProperties.SNOWLOGGED_LAYERS)) {
            boolean isBottomSnowlogged = state.getValue(BlockStateProperties.SNOWLOGGED_LAYERS) > 0;

            if (isBottomSnowlogged) {
                level.setBlock(upperPos, upperState.setValue(BlockStateProperties.BOTTOM_SNOWLOGGED, true), 2);
            }
        }
    }

    @Inject(method = "placeAt", at = @At("RETURN"))
    private static void syncSnowloggedStateOnGeneration(LevelAccessor level, BlockState state, BlockPos lowerPos, int updateType, CallbackInfo ci) {
        BlockPos upperPos = lowerPos.above();
        BlockState upperState = level.getBlockState(upperPos);
        BlockState lowerState = level.getBlockState(lowerPos);

        if (upperState.hasProperty(BlockStateProperties.BOTTOM_SNOWLOGGED) && lowerState.hasProperty(BlockStateProperties.SNOWLOGGED_LAYERS)) {
            boolean isBottomSnowlogged = lowerState.getValue(BlockStateProperties.SNOWLOGGED_LAYERS) > 0;

            if (isBottomSnowlogged) {
                level.setBlock(upperPos, upperState.setValue(BlockStateProperties.BOTTOM_SNOWLOGGED, true), updateType);
            }
        }
    }
}