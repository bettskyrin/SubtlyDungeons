package net.meander.subtlyd.mixin.common.world.level.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.meander.subtlyd.world.level.block.SimpleSnowloggedBlock;
import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin implements SimpleSnowloggedBlock {
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/StateDefinition$Builder;create(Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/StateDefinition$Factory;)Lnet/minecraft/world/level/block/state/StateDefinition;"))
    private void addSnowloggedProperty(BlockBehaviour.Properties properties, CallbackInfo ci, @Local(name = "builder") StateDefinition.Builder<Block, BlockState> builder) {
        Block block = (Block) (Object) this;

        if (block instanceof VegetationBlock || block instanceof CrossCollisionBlock || block instanceof FenceGateBlock || block instanceof WallBlock || block instanceof SegmentableBlock) {
            if (block instanceof DoublePlantBlock) {
                builder.add(BlockStatePropertiesSD.BOTTOM_SNOWLOGGED);
            }
            builder.add(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);
        }
    }

    @ModifyVariable(method = "registerDefaultState", at = @At("HEAD"), name = "state", argsOnly = true)
    private BlockState setDefaultStates(BlockState state) {
        if (state.hasProperty(BlockStatePropertiesSD.BOTTOM_SNOWLOGGED)) {
            return state.setValue(BlockStatePropertiesSD.BOTTOM_SNOWLOGGED, false);
        }

        if (state.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS)) {
            state = state.setValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, 0);
        }
        return state;
    }
}