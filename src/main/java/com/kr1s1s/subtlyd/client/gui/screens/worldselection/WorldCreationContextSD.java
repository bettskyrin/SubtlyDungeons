package com.kr1s1s.subtlyd.client.gui.screens.worldselection;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.gamerules.GameRuleMap;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.WorldOptions;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

@Environment(EnvType.CLIENT)
public record WorldCreationContextSD(
        WorldOptions options,
        Registry<LevelStem> datapackDimensions,
        WorldDimensions selectedDimensions,
        LayeredRegistryAccess<RegistryLayer> worldgenRegistries,
        ReloadableServerResources dataPackResources,
        WorldDataConfiguration dataConfiguration,
        InitialWorldCreationOptionsSD initialWorldCreationOptions
) {
    public WorldCreationContextSD(
            WorldGenSettings worldGenSettings,
            LayeredRegistryAccess<RegistryLayer> layeredRegistryAccess,
            ReloadableServerResources reloadableServerResources,
            WorldDataConfiguration worldDataConfiguration
    ) {
        this(
                worldGenSettings.options(),
                worldGenSettings.dimensions(),
                layeredRegistryAccess,
                reloadableServerResources,
                worldDataConfiguration,
                new InitialWorldCreationOptionsSD(WorldCreationUiStateSD.SelectedGameMode.SURVIVAL, GameRuleMap.of(), null)
        );
    }

    public WorldCreationContextSD(
            WorldOptions worldOptions,
            WorldDimensions worldDimensions,
            LayeredRegistryAccess<RegistryLayer> layeredRegistryAccess,
            ReloadableServerResources reloadableServerResources,
            WorldDataConfiguration worldDataConfiguration,
            InitialWorldCreationOptionsSD initialWorldCreationOptions
    ) {
        this(
                worldOptions,
                layeredRegistryAccess.getLayer(RegistryLayer.DIMENSIONS).lookupOrThrow(Registries.LEVEL_STEM),
                worldDimensions,
                layeredRegistryAccess.replaceFrom(RegistryLayer.DIMENSIONS),
                reloadableServerResources,
                worldDataConfiguration,
                initialWorldCreationOptions
        );
    }

    public WorldCreationContextSD withSettings(WorldOptions worldOptions, WorldDimensions worldDimensions) {
        return new WorldCreationContextSD(
                worldOptions,
                this.datapackDimensions,
                worldDimensions,
                this.worldgenRegistries,
                this.dataPackResources,
                this.dataConfiguration,
                this.initialWorldCreationOptions
        );
    }

    public WorldCreationContextSD withOptions(WorldCreationContextSD.OptionsModifier optionsModifier) {
        return new WorldCreationContextSD(
                optionsModifier.apply(this.options),
                this.datapackDimensions,
                this.selectedDimensions,
                this.worldgenRegistries,
                this.dataPackResources,
                this.dataConfiguration,
                this.initialWorldCreationOptions
        );
    }

    public WorldCreationContextSD withDimensions(WorldCreationContextSD.DimensionsUpdater dimensionsUpdater) {
        return new WorldCreationContextSD(
                this.options,
                this.datapackDimensions,
                (WorldDimensions) dimensionsUpdater.apply(this.worldgenLoadContext(), this.selectedDimensions),
                this.worldgenRegistries,
                this.dataPackResources,
                this.dataConfiguration,
                this.initialWorldCreationOptions
        );
    }

    public RegistryAccess.Frozen worldgenLoadContext() {
        return this.worldgenRegistries.compositeAccess();
    }

    public void validate() {
        for (LevelStem levelStem : this.datapackDimensions()) {
            levelStem.generator().validate();
        }
    }

    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface DimensionsUpdater extends BiFunction<RegistryAccess.Frozen, WorldDimensions, WorldDimensions> {
    }

    @Environment(EnvType.CLIENT)
    public interface OptionsModifier extends UnaryOperator<WorldOptions> {
    }
}