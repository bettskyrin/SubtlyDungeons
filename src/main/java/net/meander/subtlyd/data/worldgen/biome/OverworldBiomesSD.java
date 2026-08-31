package net.meander.subtlyd.data.worldgen.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * @see net.minecraft.data.worldgen.biome.OverworldBiomes
 */
public class OverworldBiomesSD {
    public static Biome gravelBeach(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<WorldCarver> worldCarvers = context.lookup(Registries.CARVER);

        BiomeGenerationSettings.Builder genBuilder = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
        MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();

        BiomeDefaultFeatures.commonSpawns(mobs);

        BiomeDefaultFeatures.addDefaultCarversAndLakes(genBuilder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(genBuilder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(genBuilder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(genBuilder);
        BiomeDefaultFeatures.addDefaultSprings(genBuilder);
        BiomeDefaultFeatures.addSurfaceFreezing(genBuilder);
        BiomeDefaultFeatures.addDefaultOres(genBuilder);
        BiomeDefaultFeatures.addDefaultSoftDisks(genBuilder);


        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.8F)
                .downfall(0.4F)
                .specialEffects(new BiomeSpecialEffects.Builder().waterColor(0x3f76e4).build())
                .mobSpawnSettings(mobs.build())
                .generationSettings(genBuilder.build())
                .build();
    }
}
