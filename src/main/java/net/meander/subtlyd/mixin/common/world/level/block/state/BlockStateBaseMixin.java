package net.meander.subtlyd.mixin.common.world.level.block.state;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.ConcurrentHashMap;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
    private static final ConcurrentHashMap<VoxelShape, VoxelShape[]> SHAPE_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<VoxelShape, VoxelShape[]> COLLISION_CACHE = new ConcurrentHashMap<>();
    private static final VoxelShape[] SNOW_SHAPES = new VoxelShape[9];

    static {
        for (int i = 1; i <= 8; i++) {
            SNOW_SHAPES[i] = Block.box(0.0, 0.0, 0.0, 16.0, i * 2.0, 16.0);
        }
    }

    @Inject(method = "getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("RETURN"), cancellable = true)
    private void setSnowloggedShape(BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        @SuppressWarnings("DataFlowIssue")
        BlockState state = (BlockState) (Object) this;

        if (state.hasProperty(BlockStateProperties.SNOWLOGGED_LAYERS)) {
            int layers = state.getValue(BlockStateProperties.SNOWLOGGED_LAYERS);

            if (layers > 0) {
                if (layers == BlockStateProperties.SNOWLOGGED_LAYERS.getPossibleValues().size()) {
                    cir.setReturnValue(Shapes.block());
                } else {
                    VoxelShape baseShape = cir.getReturnValue();
                    VoxelShape[] cachedShapes = SHAPE_CACHE.computeIfAbsent(baseShape, _ -> new VoxelShape[9]);

                    if (cachedShapes[layers] == null) {
                        cachedShapes[layers] = Shapes.or(baseShape, SNOW_SHAPES[layers]);
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

        if (state.hasProperty(BlockStateProperties.SNOWLOGGED_LAYERS)) {
            int layers = state.getValue(BlockStateProperties.SNOWLOGGED_LAYERS);

            if (layers > 0) {
                if (layers == BlockStateProperties.SNOWLOGGED_LAYERS.getPossibleValues().size()) {
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

        if (state.hasProperty(BlockStateProperties.SNOWLOGGED_LAYERS) && state.getValue(BlockStateProperties.SNOWLOGGED_LAYERS) > 0) {
            if (state.getBlock() instanceof FenceBlock) {
                cir.setReturnValue(Shapes.empty());
            }
        }
    }

    @Inject(method = "canBeReplaced()Z", at = @At("HEAD"), cancellable = true)
    private void disableSnowloggedBlockReplacement(CallbackInfoReturnable<Boolean> cir) {
        @SuppressWarnings("DataFlowIssue")
        BlockState state = (BlockState) (Object) this;

        if (state.hasProperty(BlockStateProperties.SNOWLOGGED_LAYERS) && state.getValue(BlockStateProperties.SNOWLOGGED_LAYERS) > 0) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getOffset", at = @At("HEAD"), cancellable = true)
    private void removeXZOffset(BlockPos pos, CallbackInfoReturnable<Vec3> cir) {
        @SuppressWarnings("DataFlowIssue")
        BlockState state = (BlockState) (Object) this;

        if (state.hasProperty(BlockStateProperties.SNOWLOGGED_LAYERS) && state.getValue(BlockStateProperties.SNOWLOGGED_LAYERS) > 0) {
            cir.setReturnValue(Vec3.ZERO);
        } else if (state.hasProperty(BlockStateProperties.BOTTOM_SNOWLOGGED) && state.getValue(BlockStateProperties.BOTTOM_SNOWLOGGED)) {
            cir.setReturnValue(Vec3.ZERO);
        }
    }

    @Inject(method = "getLightEmission", at = @At("HEAD"), cancellable = true)
    private void setLightEmission(CallbackInfoReturnable<Integer> cir) {
        @SuppressWarnings("DataFlowIssue")
        BlockState state = (BlockState) (Object) this;

        if (state.is(Blocks.BROWN_MUSHROOM_BLOCK)) {
            cir.setReturnValue(1);
        }
    }
}