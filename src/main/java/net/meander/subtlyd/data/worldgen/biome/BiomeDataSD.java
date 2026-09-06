package net.meander.subtlyd.data.worldgen.biome;

import net.meander.subtlyd.world.level.biome.BiomesSD;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;

/**
 * @see net.minecraft.data.worldgen.biome.BiomeData
 */
public class BiomeDataSD {
    public static void bootstrap(BootstrapContext<Biome> context) {
        context.register(BiomesSD.GRAVEL_BEACH, OverworldBiomesSD.gravelBeach(context));
        context.register(BiomesSD.WARM_RIVER, OverworldBiomesSD.warmRiver(context));
    }
}
