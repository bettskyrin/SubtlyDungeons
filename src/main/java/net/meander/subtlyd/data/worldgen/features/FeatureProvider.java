package net.meander.subtlyd.data.worldgen.features;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.levelgen.placement.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class FeatureProvider extends FabricDynamicRegistryProvider {
    public FeatureProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.@NotNull Provider registries, Entries entries) {
        HolderLookup.RegistryLookup<PlacedFeature> placedFeatures = registries.lookupOrThrow(Registries.PLACED_FEATURE);

        birchTall(placedFeatures, entries);
        brownMushroomNormal(placedFeatures, entries);
        patchBush(placedFeatures, entries);
        patchGrassPlain(placedFeatures, entries);
        patchGrassTaiga2(placedFeatures, entries);
        patchTallGrass(placedFeatures, entries);
        redMushroomNormal(placedFeatures, entries);
        treesBirch(placedFeatures, entries);
        treesBirchAndOakLeafLitter(placedFeatures, entries);
        entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
        entries.addAll(registries.lookupOrThrow(Registries.FEATURE));
    }

    private void replaceModifiers(HolderLookup.RegistryLookup<PlacedFeature> registry, Entries entries, ResourceKey<PlacedFeature> featureKey, Function<PlacementModifier, PlacementModifier> replacer) {
        try {
            PlacedFeature placedFeature = registry.getOrThrow(featureKey).value();
            List<PlacementModifier> modifiers = placedFeature.placement().stream().map(replacer).toList();

            entries.add(featureKey, new PlacedFeature(placedFeature.feature(), modifiers));
        } catch (Exception e) {
            UtilSD.LOGGER.error("Failed to override {}", featureKey.identifier(), e);
        }
    }

    private void brownMushroomNormal(HolderLookup.RegistryLookup<PlacedFeature> registry, Entries entries) {
        replaceModifiers(registry, entries, VegetationPlacements.BROWN_MUSHROOM_NORMAL, modifier -> switch (modifier) {
            case RarityFilter _ -> RarityFilter.onAverageOnceEvery(32);
            case CountPlacement _ -> CountPlacement.of(96);
            default -> modifier;
        });
    }

    private void patchBush(HolderLookup.RegistryLookup<PlacedFeature> registry, Entries entries) {
        replaceModifiers(registry, entries, VegetationPlacements.PATCH_BUSH, modifier -> switch (modifier) {
            case RarityFilter _ -> RarityFilter.onAverageOnceEvery(2);
            case CountPlacement _ -> CountPlacement.of(24);
            default -> modifier;
        });
    }

    private void patchGrassPlain(HolderLookup.RegistryLookup<PlacedFeature> registry, Entries entries) {
        replaceModifiers(registry, entries, VegetationPlacements.PATCH_GRASS_PLAIN, modifier -> switch (modifier) {
            case NoiseThresholdCountPlacement _ -> NoiseThresholdCountPlacement.of(-0.8, 9, 15);
            case CountPlacement _ -> CountPlacement.of(32);
            default -> modifier;
        });
    }

    private void patchGrassTaiga2(HolderLookup.RegistryLookup<PlacedFeature> registry, Entries entries) {
        replaceModifiers(registry, entries, VegetationPlacements.PATCH_GRASS_TAIGA_2, modifier -> switch (modifier) {
            case CountPlacement _ -> CountPlacement.of(35);
            default -> modifier;
        });
    }

    private void patchTallGrass(HolderLookup.RegistryLookup<PlacedFeature> registry, Entries entries) {
        replaceModifiers(registry, entries, VegetationPlacements.PATCH_TALL_GRASS, modifier -> switch (modifier) {
            case RarityFilter _ -> RarityFilter.onAverageOnceEvery(6);
            case CountPlacement _ -> CountPlacement.of(96);
            default -> modifier;
        });
    }

    private void redMushroomNormal(HolderLookup.RegistryLookup<PlacedFeature> registry, Entries entries) {
        replaceModifiers(registry, entries, VegetationPlacements.RED_MUSHROOM_NORMAL, modifier -> switch (modifier) {
            case RarityFilter _ -> RarityFilter.onAverageOnceEvery(64);
            case CountPlacement _ -> CountPlacement.of(96);
            default -> modifier;
        });
    }

    private void birchTall(HolderLookup.RegistryLookup<PlacedFeature> registry, Entries entries) {
        ResourceKey<PlacedFeature> placedFeature = VegetationPlacements.BIRCH_TALL;

        try {
            PlacedFeature original = registry.getOrThrow(placedFeature).value();
            List<PlacementModifier> modifiers = new ArrayList<>(original.placement());

            modifiers.set(0, CountPlacement.of(new WeightedListInt(
                    WeightedList.<IntProvider>builder()
                            .add(ConstantInt.of(10), 9)
                            .add(ConstantInt.of(12), 1)
                            .build()
            )));

            entries.add(placedFeature, new PlacedFeature(original.feature(), modifiers));
        } catch (Exception e) {
            UtilSD.LOGGER.error("Failed to override {}", placedFeature.identifier(), e);
        }
    }

    private void treesBirch(HolderLookup.RegistryLookup<PlacedFeature> registry, Entries entries) {
        ResourceKey<PlacedFeature> placedFeature = VegetationPlacements.TREES_BIRCH;

        try {
            PlacedFeature original = registry.getOrThrow(placedFeature).value();
            List<PlacementModifier> modifiers = new ArrayList<>(original.placement());

            modifiers.set(0, CountPlacement.of(new WeightedListInt(
                    WeightedList.<IntProvider>builder()
                            .add(ConstantInt.of(10), 9)
                            .add(ConstantInt.of(11), 1)
                            .build()
            )));

            entries.add(placedFeature, new PlacedFeature(original.feature(), modifiers));
        } catch (Exception e) {
            UtilSD.LOGGER.error("Failed to override {}", placedFeature.identifier(), e);
        }
    }

    private void treesBirchAndOakLeafLitter(HolderLookup.RegistryLookup<PlacedFeature> registry, Entries entries) {
        ResourceKey<PlacedFeature> placedFeature = VegetationPlacements.TREES_BIRCH_AND_OAK_LEAF_LITTER;

        try {
            PlacedFeature original = registry.getOrThrow(placedFeature).value();
            List<PlacementModifier> modifiers = new ArrayList<>(original.placement());

            modifiers.set(0, CountPlacement.of(new WeightedListInt(
                    WeightedList.<IntProvider>builder()
                            .add(ConstantInt.of(12), 8)
                            .add(ConstantInt.of(10), 2)
                            .build()
            )));

            entries.add(placedFeature, new PlacedFeature(original.feature(), modifiers));
        } catch (Exception e) {
            UtilSD.LOGGER.error("Failed to override {}", placedFeature.identifier(), e);
        }
    }
    @Override
    public @NotNull String getName() {
        return "Modified World Generation Feature Provider";
    }
}