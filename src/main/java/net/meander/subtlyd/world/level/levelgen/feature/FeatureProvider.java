package net.meander.subtlyd.world.level.levelgen.feature;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Acts as the Data Generation provider for dynamic world generation registries.
 * This class reads the bootstraps defined in ConfiguredFeaturesSD and PlacedFeaturesSD
 * and exports them into JSON files for the classic data-driven architecture.
 */
public class FeatureProvider extends FabricDynamicRegistryProvider {
    public FeatureProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.@NotNull Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(Registries.CONFIGURED_FEATURE));
        entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
    }

    @Override
    public @NotNull String getName() {
        return "World Generation Features";
    }
}