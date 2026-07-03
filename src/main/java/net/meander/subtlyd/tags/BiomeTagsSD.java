package net.meander.subtlyd.tags;

import net.meander.subtlyd.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

/**
 * @see net.minecraft.tags.BiomeTags
 */
public class BiomeTagsSD {
    public static final TagKey<Biome> IS_WINDY = create("is_windy");
    public static final TagKey<Biome> IS_VERY_FOGGY = create("is_very_foggy");
    public static final TagKey<Biome> IS_FOGGY = create("is_foggy");
    public static final TagKey<Biome> HAS_CESPITOSE = create("has_cespitose");

    private static TagKey<Biome> create(String string) {
        return TagKey.create(Registries.BIOME, Util.identifier(string));
    }
}
