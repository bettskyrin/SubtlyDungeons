package net.meander.subtlyd.mixin.common.world.level.block.state;

import net.meander.subtlyd.data.models.blockstates.SnowloggableBlocks;
import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
    @Inject(
        method = "getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
        at = @At("RETURN"),
        cancellable = true)
    private void getSnowloggedShape(BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        @SuppressWarnings("DataFlowIssue")
        BlockState state = (BlockState) (Object) this;

        if (state.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS)) {
            int layers = state.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);

            if (layers > 0) {
                if (layers == SnowloggableBlocks.MAX_LAYERS) {
                    cir.setReturnValue(Shapes.block());
                } else {
                    VoxelShape snowShape = Block.box(0.0, 0.0, 0.0, 16.0, layers * 2.0, 16.0);
                    cir.setReturnValue(Shapes.or(cir.getReturnValue(), snowShape));
                }
            }
        }
    }

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("RETURN"), cancellable = true
    )
    private void getSnowloggedCollisionShape(BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        @SuppressWarnings("DataFlowIssue")
        BlockState state = (BlockState) (Object) this;

        if (state.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS)) {
            int layers = state.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);

            if (layers > 0) {
                if (layers == 8) {
                    cir.setReturnValue(Shapes.block());
                } else if (layers > 1) {
                    VoxelShape snowCollision = Block.box(0.0D, 0.0D, 0.0D, 16.0D, (layers - 1) * 2.0D, 16.0D);
                    cir.setReturnValue(Shapes.or(cir.getReturnValue(), snowCollision));
                }
            }
        }
    }

    @Inject(
            method = "getOcclusionShape",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ignoreSnowForOcclusion(CallbackInfoReturnable<VoxelShape> cir) {
        @SuppressWarnings("DataFlowIssue")
        BlockState state = (BlockState) (Object) this;
        if (state.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS)) {
            int layers = state.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);

            if (layers > 0 && layers < SnowloggableBlocks.MAX_LAYERS) {
                cir.setReturnValue(state.getBlock().defaultBlockState().getOcclusionShape());
            }
        }
    }

    @Inject(method = "canBeReplaced(Lnet/minecraft/world/item/context/BlockPlaceContext;)Z", at = @At("HEAD"), cancellable = true)
    private void disableSnowloggedBlockReplacement(BlockPlaceContext context, CallbackInfoReturnable<Boolean> cir) {
        @SuppressWarnings("DataFlowIssue")
        BlockState state = (BlockState) (Object) this;

        if (state.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) && state.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) > 0) {
            cir.setReturnValue(false);
        }
    }

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

        if (minecraft.level != null && minecraft.level.isClientSide()) {
            return minecraft.level.getBlockState(pos);
        }
        return null;
    }
}