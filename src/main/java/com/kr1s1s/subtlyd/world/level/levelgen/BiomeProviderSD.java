package com.kr1s1s.subtlyd.world.level.levelgen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class BiomeProviderSD extends FabricDynamicRegistryProvider {
    public BiomeProviderSD(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.add(BiomesSD.REEDS_CONFIGURED_FEATURE, BiomesSD.REEDS_CONFIGURED);
        entries.add(BiomesSD.REEDS_PLACED_FEATURE, BiomesSD.REEDS_PLACED);
    }

    @Override
    public String getName() {
        return "World Generation";
    }
}
