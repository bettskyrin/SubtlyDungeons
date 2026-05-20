package net.meander.subtlyd.mixin.common.world.level.block.state;

import net.meander.subtlyd.data.models.blockstates.SnowloggableBlocks;
import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
    /**
     * Sets the selection outline shape
     */
    private static final Map<VoxelShape, VoxelShape[]> SHAPE_CACHE = new ConcurrentHashMap<>();
    private static final Map<VoxelShape, VoxelShape[]> COLLISION_CACHE = new ConcurrentHashMap<>();

    @Inject(method = "getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("RETURN"), cancellable = true)
    private void setSnowloggedShape(BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        @SuppressWarnings("DataFlowIssue")
        BlockState state = (BlockState) (Object) this;

        if (state.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS)) {
            int layers = state.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);

            if (layers > 0) {
                if (layers == SnowloggableBlocks.MAX_LAYERS) {
                    cir.setReturnValue(Shapes.block());
                } else {
                    VoxelShape baseShape = cir.getReturnValue();
                    VoxelShape[] cachedShapes = SHAPE_CACHE.computeIfAbsent(baseShape, _ -> new VoxelShape[9]);

                    if (cachedShapes[layers] == null) {
                        VoxelShape snowShape = Block.box(0.0, 0.0, 0.0, 16.0, layers * 2.0, 16.0);
                        cachedShapes[layers] = Shapes.or(baseShape, snowShape);
                    }

                    cir.setReturnValue(cachedShapes[layers]);
                }
            }
        }
    }

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("RETURN"), cancellable = true)
    private void setSnowloggedCollisionShape(BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        @SuppressWarnings("DataFlowIssue")
        BlockState state = (BlockState) (Object) this;

        if (state.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS)) {
            int layers = state.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);

            if (layers > 0) {
                if (layers == SnowloggableBlocks.MAX_LAYERS) {
                    cir.setReturnValue(Shapes.block());
                } else {
                    VoxelShape baseShape = cir.getReturnValue();
                    VoxelShape[] cachedCollisions = COLLISION_CACHE.computeIfAbsent(baseShape, _ -> new VoxelShape[9]);

                    if (cachedCollisions[layers] == null) {
                        VoxelShape snowCollision = Block.box(0.0D, 0.0D, 0.0D, 16.0D, (layers - 1) * 2.0D, 16.0D);
                        cachedCollisions[layers] = Shapes.or(baseShape, snowCollision);
                    }
                    cir.setReturnValue(cachedCollisions[layers]);
                }
            }
        }
    }

    @Inject(method = "getFaceOcclusionShape", at = @At("HEAD"), cancellable = true)
    private void disableSnowloggedFaceCulling(Direction direction, CallbackInfoReturnable<VoxelShape> cir) {
        @SuppressWarnings("DataFlowIssue")
        BlockState state = (BlockState) (Object) this;

        if (state.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) && state.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) > 0) {
            if (state.getBlock() instanceof FenceBlock) {
                cir.setReturnValue(Shapes.empty());
            }
        }
    }

    /**
     * Prevents replaceable snowlogged blocks from being replaceable
     */
    @Inject(method = "canBeReplaced()Z", at = @At("HEAD"), cancellable = true)
    private void disableSnowloggedBlockReplacement(CallbackInfoReturnable<Boolean> cir) {
        @SuppressWarnings("DataFlowIssue")
        BlockState state = (BlockState) (Object) this;

        if (state.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) && state.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) > 0) {
            cir.setReturnValue(false);
        }
    }

    /**
     * Grass has an X-Z random offset that makes snowlogged blocks look wrong. This removes that when snowlogged.
     */
    @Inject(method = "getOffset", at = @At("HEAD"), cancellable = true)
    private void removeXZOffset(BlockPos pos, CallbackInfoReturnable<Vec3> cir) {
        @SuppressWarnings("DataFlowIssue")
        BlockState state = (BlockState) (Object) this;

        boolean isSnowlogged = state.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) && state.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) > 0;
        boolean isBottomSnowlogged = state.hasProperty(BlockStatePropertiesSD.BOTTOM_SNOWLOGGED) && state.getValue(BlockStatePropertiesSD.BOTTOM_SNOWLOGGED);

        if (isSnowlogged || isBottomSnowlogged) {
            cir.setReturnValue(Vec3.ZERO);
        }
    }
}