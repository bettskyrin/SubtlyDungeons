package net.meander.subtlyd.mixin.common.world.level.levelgen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ChorusPlantFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusPlantFeature.class)
public class ChorusPlantFeatureMixin {

    @Inject(method = "place", at = @At("RETURN"))
    private void placeLivingFlowers(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            BlockPos minPos = origin.offset(-12, 0, -12);
            BlockPos maxPos = origin.offset(12, 35, 12);

            for (BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
                BlockState state = level.getBlockState(pos);
                
                if (state.is(Blocks.CHORUS_FLOWER) && state.getValue(ChorusFlowerBlock.AGE) == 5) {
                    if (random.nextFloat() <= 0.85F) {
                        level.setBlock(pos, state.setValue(ChorusFlowerBlock.AGE, 0), 2);
                    }
                }
            }
        }
    }
}