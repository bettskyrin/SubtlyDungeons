package com.kr1s1s.subtlyd.data.tags;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.concurrent.CompletableFuture;

public class BiomeTagsSD extends BiomeTagsProvider {
    public BiomeTagsSD(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(packOutput, completableFuture);
    }

    public static final TagKey<Biome> IS_WINDY = create("is_windy");

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        this.tag(IS_WINDY)
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
    }

    private static TagKey<Biome> create(String string) {
        return TagKey.create(Registries.BIOME, SubtlyDungeons.resourceLocation(string));
    }
}
