package net.meander.subtlyd.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.level.levelgen.BiomesSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.concurrent.CompletableFuture;

public class BiomeTagsSD extends FabricTagsProvider<Biome> {
    public static final TagKey<Biome> IS_WINDY = bind("is_windy");
    public static final TagKey<Biome> IS_VERY_FOGGY = bind("is_very_foggy");
    public static final TagKey<Biome> IS_FOGGY = bind("is_foggy");
    public static final TagKey<Biome> HAS_CESPITOSE = bind("has_cespitose");

    public BiomeTagsSD(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(packOutput, Registries.BIOME, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(IS_WINDY)
                .add(Biomes.SNOWY_PLAINS)
                .add(Biomes.ICE_SPIKES)
                .add(Biomes.FROZEN_OCEAN)
                .add(Biomes.SNOWY_TAIGA)
                .add(Biomes.FROZEN_RIVER)
                .add(Biomes.SNOWY_BEACH)
                .add(Biomes.FROZEN_PEAKS)
                .add(Biomes.JAGGED_PEAKS)
                .add(Biomes.SNOWY_SLOPES)
                .add(Biomes.GROVE);
        tag(IS_VERY_FOGGY)
                .add(Biomes.PALE_GARDEN);
        tag(IS_FOGGY)
                .add(Biomes.DARK_FOREST)
                .add(Biomes.SWAMP)
                .add(Biomes.MANGROVE_SWAMP);
        tag(HAS_CESPITOSE)
                .add(Biomes.DARK_FOREST);
        tag(BiomeTags.IS_RIVER)
                .addOptional(BiomesSD.COLD_RIVER)
                .addOptional(BiomesSD.WARM_RIVER);
        tag(BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS)
                .addOptional(BiomesSD.COLD_RIVER);
        tag(BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS)
                .addOptional(BiomesSD.WARM_RIVER);
        tag(BiomeTags.SPAWNS_WARM_VARIANT_FROGS)
                .addOptional(BiomesSD.WARM_RIVER);
    }

    private static TagKey<Biome> bind(String string) {
        return TagKey.create(Registries.BIOME, Util.identifier(string));
    }
}
