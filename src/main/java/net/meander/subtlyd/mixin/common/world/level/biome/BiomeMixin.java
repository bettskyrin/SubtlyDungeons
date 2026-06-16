package net.meander.subtlyd.mixin.common.world.level.biome;

import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public abstract class BiomeMixin {
    @Inject(method = "shouldSnow(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z", at = @At("RETURN"), cancellable = true)
    private void shouldSnowlogVegetation(LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            Biome biome = (Biome) (Object) this;

            if (biome.getPrecipitationAt(pos, level.getSeaLevel()) == Biome.Precipitation.SNOW) {
                if (level.isInsideBuildHeight(pos.getY()) && level.getBrightness(LightLayer.BLOCK, pos) < 10) {
                    BlockState state = level.getBlockState(pos);

                    if (state.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS) ||
                            state.hasProperty(BlockStatePropertiesSD.BOTTOM_SNOWLOGGED)) {
                        cir.setReturnValue(true);
                    }
                }
            }
        }
    }
}