package com.kr1s1s.subtlyd.mixin.server.block;

import com.kr1s1s.subtlyd.world.block.BlocksSD;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class TallGrassBlockMixin {

    @Inject(method = "updateShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/world/level/ScheduledTickAccess;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("TAIL"), cancellable = true)
    public BlockState updateShape(
            BlockState blockState,
            LevelReader levelReader,
            ScheduledTickAccess scheduledTickAccess,
            BlockPos blockPos,
            Direction direction,
            BlockPos blockPos2,
            BlockState blockState2,
            RandomSource randomSource, CallbackInfoReturnable<BlockState> cir
    ) {
        if (isSnowySetting(blockState2) && blockState.is(Blocks.SHORT_GRASS)) { // Snowy Grass
            blockState = BlocksSD.CHARCOAL_BLOCK.defaultBlockState(); // TODO
        } else if (!isSnowySetting(blockState2) && blockState.is(BlocksSD.CHARCOAL_BLOCK)) {
            blockState = Blocks.TALL_GRASS.defaultBlockState(); // TODO
        }
        return blockState;
    }

    private static boolean isSnowySetting(BlockState blockState) {
        return blockState.is(BlockTags.SNOW);
    }
}
