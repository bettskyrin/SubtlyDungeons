package net.meander.subtlyd.data.worldgen.placement;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class PlacementUtilsSD {
    public static ResourceKey<PlacedFeature> createKey(final String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, UtilSD.identifier(name));
    }
}
