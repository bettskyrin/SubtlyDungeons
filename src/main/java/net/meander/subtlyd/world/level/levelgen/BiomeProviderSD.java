package net.meander.subtlyd.world.level.levelgen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.meander.subtlyd.world.level.levelgen.feature.ReedsFeature;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class BiomeProviderSD extends FabricDynamicRegistryProvider {
    public BiomeProviderSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.@NotNull Provider registries, Entries entries) {
        entries.add(ReedsFeature.REEDS_CONFIGURED_FEATURE, ReedsFeature.REEDS_CONFIGURED);
        entries.add(ReedsFeature.REEDS_PLACED_FEATURE, ReedsFeature.REEDS_PLACED);
    }

    @Override
    public @NotNull String getName() {
        return "World Generation";
    }
}
