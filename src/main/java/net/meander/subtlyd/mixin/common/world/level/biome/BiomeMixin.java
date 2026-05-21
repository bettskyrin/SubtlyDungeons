package net.meander.subtlyd.mixin.common.world.level.biome;

import net.meander.subtlyd.world.level.block.SimpleSnowloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public abstract class BiomeMixin {
    @Shadow public abstract boolean coldEnoughToSnow(BlockPos pos, int seaLevel);

    @Inject(method = "shouldSnow", at = @At("TAIL"), cancellable = true)
    private void shouldSnowlogVegetation(LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState state = level.getBlockState(pos);

        if (SimpleSnowloggedBlock.isSnowloggable(state.getBlock())) {
            if (coldEnoughToSnow(pos, level.getSeaLevel()) && level.getBrightness(LightLayer.BLOCK, pos) < 10) {
                cir.setReturnValue(level.isInsideBuildHeight(pos));
            }
        }
    }
}