package net.meander.subtlyd.mixin.common.world.level.biome;

import net.meander.subtlyd.world.level.biome.BiomesSD;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OverworldBiomeBuilder.class)
public class OverworldBiomeBuilderMixin {
    @Inject(method = "pickBeachBiome", at = @At("RETURN"), cancellable = true)
    private void pickGravelBeach(int temperatureIndex, int humidityIndex, CallbackInfoReturnable<ResourceKey<Biome>> cir) {
        if (cir.getReturnValue() == Biomes.BEACH) {
            if (temperatureIndex == 1) {
                cir.setReturnValue(BiomesSD.GRAVEL_BEACH);
            }
        }
    }
}