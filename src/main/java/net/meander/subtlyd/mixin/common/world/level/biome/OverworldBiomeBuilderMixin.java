package net.meander.subtlyd.mixin.common.world.level.biome;

import com.mojang.datafixers.util.Pair;
import net.meander.subtlyd.world.level.levelgen.BiomesSD;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Consumer;

@Mixin(OverworldBiomeBuilder.class)
public class OverworldBiomeBuilderMixin {
    private final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);
    private final Climate.Parameter[] temperatureRanges = new Climate.Parameter[] {
            Climate.Parameter.span(-1.0F, -0.45F),
            Climate.Parameter.span(-0.45F, -0.3F),
            Climate.Parameter.span(-0.15F, 0.2F),
            Climate.Parameter.span(0.2F, 0.55F),
            Climate.Parameter.span(0.5F, 1.0F)
    };

    @ModifyVariable(method = "addBiomes", at = @At("HEAD"), argsOnly = true, name = "biomes")
    private Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> addRivers(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes) {
        final Climate.Parameter riverContinentalness = Climate.Parameter.span(-0.11F, 0.55F);
        final Climate.Parameter riverErosion = Climate.Parameter.span(-0.375F, 1.0F);
        final Climate.Parameter riverWeirdness = Climate.Parameter.span(-0.06F, 0.06F);

        if (System.getProperty("fabric-api.datagen") != null) {
            return biomes;
        }

        return pair -> {
            ResourceKey<Biome> biomeKey = pair.getSecond();
            Climate.ParameterPoint p = pair.getFirst();

            if (biomeKey.equals(Biomes.RIVER)) {
                biomes.accept(Pair.of(Climate.parameters(
                        temperatureRanges[1],
                        FULL_RANGE,
                        riverContinentalness,
                        riverErosion,
                        p.depth(),
                        riverWeirdness,
                        0.0F
                ), BiomesSD.COLD_RIVER));

                biomes.accept(Pair.of(Climate.parameters(
                        temperatureRanges[4],
                        Climate.Parameter.span(-1.0F, -0.15F),
                        riverContinentalness,
                        riverErosion,
                        p.depth(),
                        riverWeirdness,
                        0.0F
                ), BiomesSD.WARM_RIVER));

                biomes.accept(Pair.of(Climate.parameters(
                        Climate.Parameter.span(-0.3F, 0.5F),
                        FULL_RANGE,
                        riverContinentalness,
                        riverErosion,
                        p.depth(),
                        riverWeirdness,
                        0.0F
                ), Biomes.RIVER));

                biomes.accept(Pair.of(Climate.parameters(
                        temperatureRanges[4],
                        Climate.Parameter.span(-0.15F, 1.0F),
                        riverContinentalness,
                        riverErosion,
                        p.depth(),
                        riverWeirdness,
                        0.0F
                ), Biomes.RIVER));

            } else {
                biomes.accept(pair);
            }
        };
    }
}