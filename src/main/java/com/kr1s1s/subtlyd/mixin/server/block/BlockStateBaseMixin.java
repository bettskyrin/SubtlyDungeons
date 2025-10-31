package com.kr1s1s.subtlyd.mixin.server.block;

import com.kr1s1s.subtlyd.world.block.BlocksSD;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {

    @Shadow
    public abstract Block getBlock();

    @Inject(method = "getBlock", at = @At("TAIL"), cancellable = true)
    public void getBlock(CallbackInfoReturnable<Block> cir) {
        BlockState block = (BlockState) (Object) this;

        if (block.is(BlocksSD.SHORT_GRASS_BLOCK_SNOWY)) {
            cir.setReturnValue(Blocks.SHORT_GRASS);
        } else if (block.is(BlocksSD.TALL_GRASS_BLOCK_SNOWY)) {
            cir.setReturnValue(Blocks.TALL_GRASS);
        }
    }
}
