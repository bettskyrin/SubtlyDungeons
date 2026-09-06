package net.meander.subtlyd.world.level.biome;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

/**
 * @see net.minecraft.world.level.biome.Biomes
 */
public class BiomesSD {
    public static final ResourceKey<Biome> GRAVEL_BEACH = register("gravel_beach");
    public static final ResourceKey<Biome> WARM_RIVER = register("warm_river");

    public static void registration() {
        UtilSD.LOGGER.debug("Registering biomes...");
    }

    private static ResourceKey<Biome> register(final String name) {
        return ResourceKey.create(Registries.BIOME, UtilSD.identifier(name));
    }
}
