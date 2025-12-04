package com.kr1s1s.subtlyd.client.gui.screens.worldselection;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.worldselection.PresetEditor;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.WorldPresetTags;
import net.minecraft.util.FileUtil;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class WorldCreationUiStateSD {
    private static final Component DEFAULT_WORLD_NAME = Component.translatable("selectWorld.newWorld");
    private final List<Consumer<WorldCreationUiStateSD>> listeners = new ArrayList();
    private String name = DEFAULT_WORLD_NAME.getString();
    private WorldCreationUiStateSD.SelectedGameMode gameMode = WorldCreationUiStateSD.SelectedGameMode.SURVIVAL;
    private Difficulty difficulty = Difficulty.NORMAL;
    @Nullable
    private Boolean allowCommands;
    private String seed;
    private boolean generateStructures;
    private boolean bonusChest;
    private final Path savesFolder;
    private String targetFolder;
    private WorldCreationContextSD settings;
    private WorldCreationUiStateSD.WorldTypeEntry worldType;
    private final List<WorldCreationUiStateSD.WorldTypeEntry> normalPresetList = new ArrayList();
    private final List<WorldCreationUiStateSD.WorldTypeEntry> altPresetList = new ArrayList();
    private GameRules gameRules;

    public WorldCreationUiStateSD(Path path, WorldCreationContextSD worldCreationContext, Optional<ResourceKey<WorldPreset>> optional, OptionalLong optionalLong) {
        this.savesFolder = path;
        this.settings = worldCreationContext;
        this.worldType = new WorldCreationUiStateSD.WorldTypeEntry((Holder<WorldPreset>) findPreset(worldCreationContext, optional).orElse(null));
        this.updatePresetLists();
        this.seed = optionalLong.isPresent() ? Long.toString(optionalLong.getAsLong()) : "";
        this.generateStructures = worldCreationContext.options().generateStructures();
        this.bonusChest = worldCreationContext.options().generateBonusChest();
        this.targetFolder = this.findResultFolder(this.name);
        this.gameMode = worldCreationContext.initialWorldCreationOptions().selectedGameMode();
        this.gameRules = new GameRules(worldCreationContext.dataConfiguration().enabledFeatures());
        this.gameRules.setAll(worldCreationContext.initialWorldCreationOptions().gameRuleOverwrites(), null);
        Optional.ofNullable(worldCreationContext.initialWorldCreationOptions().flatLevelPreset())
                .flatMap(
                        resourceKey -> worldCreationContext.worldgenLoadContext().lookup(Registries.FLAT_LEVEL_GENERATOR_PRESET).flatMap(registry -> registry.get(resourceKey))
                )
                .map(reference -> ((FlatLevelGeneratorPreset) reference.value()).settings())
                .ifPresent(flatLevelGeneratorSettings -> this.updateDimensions(PresetEditorSD.flatWorldConfigurator(flatLevelGeneratorSettings)));
    }

    public void addListener(Consumer<WorldCreationUiStateSD> consumer) {
        this.listeners.add(consumer);
    }

    public void onChanged() {
        boolean bl = this.isBonusChest();
        if (bl != this.settings.options().generateBonusChest()) {
            this.settings = this.settings.withOptions(worldOptions -> worldOptions.withBonusChest(bl));
        }

        boolean bl2 = this.isGenerateStructures();
        if (bl2 != this.settings.options().generateStructures()) {
            this.settings = this.settings.withOptions(worldOptions -> worldOptions.withStructures(bl2));
        }

        for (Consumer<WorldCreationUiStateSD> consumer : this.listeners) {
            consumer.accept(this);
        }
    }

    public void setName(String string) {
        this.name = string;
        this.targetFolder = this.findResultFolder(string);
        this.onChanged();
    }

    private String findResultFolder(String string) {
        String string2 = string.trim();

        try {
            return FileUtil.findAvailableName(this.savesFolder, !string2.isEmpty() ? string2 : DEFAULT_WORLD_NAME.getString(), "");
        } catch (Exception var5) {
            try {
                return FileUtil.findAvailableName(this.savesFolder, "World", "");
            } catch (IOException var4) {
                throw new RuntimeException("Could not create save folder", var4);
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public String getTargetFolder() {
        return this.targetFolder;
    }

    public void setGameMode(WorldCreationUiStateSD.SelectedGameMode selectedGameMode) {
        this.gameMode = selectedGameMode;
        this.onChanged();
    }

    public WorldCreationUiStateSD.SelectedGameMode getGameMode() {
        return this.isDebug() ? WorldCreationUiStateSD.SelectedGameMode.DEBUG : this.gameMode;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.onChanged();
    }

    public Difficulty getDifficulty() {
        return this.isHardcore() ? Difficulty.HARD : this.difficulty;
    }

    public boolean isHardcore() {
        return this.getGameMode() == WorldCreationUiStateSD.SelectedGameMode.HARDCORE;
    }

    public void setAllowCommands(boolean bl) {
        this.allowCommands = bl;
        this.onChanged();
    }

    public boolean isAllowCommands() {
        if (this.isDebug()) {
            return true;
        } else if (this.isHardcore()) {
            return false;
        } else {
            return this.allowCommands == null ? this.getGameMode() == WorldCreationUiStateSD.SelectedGameMode.CREATIVE : this.allowCommands;
        }
    }

    public void setSeed(String string) {
        this.seed = string;
        this.settings = this.settings.withOptions(worldOptions -> worldOptions.withSeed(WorldOptions.parseSeed(this.getSeed())));
        this.onChanged();
    }

    public String getSeed() {
        return this.seed;
    }

    public void setGenerateStructures(boolean bl) {
        this.generateStructures = bl;
        this.onChanged();
    }

    public boolean isGenerateStructures() {
        return this.isDebug() ? false : this.generateStructures;
    }

    public void setBonusChest(boolean bl) {
        this.bonusChest = bl;
        this.onChanged();
    }

    public boolean isBonusChest() {
        return !this.isDebug() && !this.isHardcore() ? this.bonusChest : false;
    }

    public void setSettings(WorldCreationContextSD worldCreationContext) {
        this.settings = worldCreationContext;
        this.updatePresetLists();
        this.onChanged();
    }

    public WorldCreationContextSD getSettings() {
        return this.settings;
    }

    public void updateDimensions(WorldCreationContextSD.DimensionsUpdater dimensionsUpdater) {
        this.settings = this.settings.withDimensions(dimensionsUpdater);
        this.onChanged();
    }

    protected boolean tryUpdateDataConfiguration(WorldDataConfiguration worldDataConfiguration) {
        WorldDataConfiguration worldDataConfiguration2 = this.settings.dataConfiguration();
        if (worldDataConfiguration2.dataPacks().getEnabled().equals(worldDataConfiguration.dataPacks().getEnabled())
                && worldDataConfiguration2.enabledFeatures().equals(worldDataConfiguration.enabledFeatures())) {
            this.settings = new WorldCreationContextSD(
                    this.settings.options(),
                    this.settings.datapackDimensions(),
                    this.settings.selectedDimensions(),
                    this.settings.worldgenRegistries(),
                    this.settings.dataPackResources(),
                    worldDataConfiguration,
                    this.settings.initialWorldCreationOptions()
            );
            return true;
        } else {
            return false;
        }
    }

    public boolean isDebug() {
        return this.settings.selectedDimensions().isDebug();
    }

    public void setWorldType(WorldCreationUiStateSD.WorldTypeEntry worldTypeEntry) {
        this.worldType = worldTypeEntry;
        Holder<WorldPreset> holder = worldTypeEntry.preset();
        if (holder != null) {
            this.updateDimensions((frozen, worldDimensions) -> holder.value().createWorldDimensions());
        }
    }

    public WorldCreationUiStateSD.WorldTypeEntry getWorldType() {
        return this.worldType;
    }

    @Nullable
    public PresetEditorSD getPresetEditor() {
        Holder<WorldPreset> holder = this.getWorldType().preset();
        return holder != null ? PresetEditorSD.EDITORS.get(holder.unwrapKey()) : null;
    }

    public List<WorldCreationUiStateSD.WorldTypeEntry> getNormalPresetList() {
        return this.normalPresetList;
    }

    public List<WorldCreationUiStateSD.WorldTypeEntry> getAltPresetList() {
        return this.altPresetList;
    }

    private void updatePresetLists() {
        Registry<WorldPreset> registry = this.getSettings().worldgenLoadContext().lookupOrThrow(Registries.WORLD_PRESET);
        this.normalPresetList.clear();
        this.normalPresetList
                .addAll(
                        (Collection) getNonEmptyList(registry, WorldPresetTags.NORMAL)
                                .orElseGet(() -> registry.listElements().map(WorldCreationUiStateSD.WorldTypeEntry::new).toList())
                );
        this.altPresetList.clear();
        this.altPresetList.addAll((Collection) getNonEmptyList(registry, WorldPresetTags.EXTENDED).orElse(this.normalPresetList));
        Holder<WorldPreset> holder = this.worldType.preset();
        if (holder != null) {
            WorldCreationUiStateSD.WorldTypeEntry worldTypeEntry = (WorldCreationUiStateSD.WorldTypeEntry) findPreset(this.getSettings(), holder.unwrapKey())
                    .map(WorldCreationUiStateSD.WorldTypeEntry::new)
                    .orElse((WorldCreationUiStateSD.WorldTypeEntry) this.normalPresetList.getFirst());
            boolean bl = PresetEditor.EDITORS.get(holder.unwrapKey()) != null;
            if (bl) {
                this.worldType = worldTypeEntry;
            } else {
                this.setWorldType(worldTypeEntry);
            }
        }
    }

    private static Optional<Holder<WorldPreset>> findPreset(WorldCreationContextSD worldCreationContext, Optional<ResourceKey<WorldPreset>> optional) {
        return optional.flatMap(resourceKey -> worldCreationContext.worldgenLoadContext().lookupOrThrow(Registries.WORLD_PRESET).get(resourceKey));
    }

    private static Optional<List<WorldCreationUiStateSD.WorldTypeEntry>> getNonEmptyList(Registry<WorldPreset> registry, TagKey<WorldPreset> tagKey) {
        return registry.get(tagKey).map(named -> named.stream().map(WorldCreationUiStateSD.WorldTypeEntry::new).toList()).filter(list -> !list.isEmpty());
    }

    public void setGameRules(GameRules gameRules) {
        this.gameRules = gameRules;
        this.onChanged();
    }

    public GameRules getGameRules() {
        return this.gameRules;
    }

    @Environment(EnvType.CLIENT)
    public static enum SelectedGameMode {
        SURVIVAL("survival", GameType.SURVIVAL),
        HARDCORE("hardcore", GameType.SURVIVAL),
        CREATIVE("creative", GameType.CREATIVE),
        DEBUG("spectator", GameType.SPECTATOR);

        public final GameType gameType;
        public final Component displayName;
        private final Component info;

        private SelectedGameMode(final String string2, final GameType gameType) {
            this.gameType = gameType;
            this.displayName = Component.translatable("selectWorld.gameMode." + string2);
            this.info = Component.translatable("selectWorld.gameMode." + string2 + ".info");
        }

        public Component getInfo() {
            return this.info;
        }
    }

    @Environment(EnvType.CLIENT)
    public record WorldTypeEntry(@Nullable Holder<WorldPreset> preset) {
        private static final Component CUSTOM_WORLD_DESCRIPTION = Component.translatable("generator.custom");

        public Component describePreset() {
            return (Component) Optional.ofNullable(this.preset)
                    .flatMap(Holder::unwrapKey)
                    .map(resourceKey -> Component.translatable(resourceKey.identifier().toLanguageKey("generator")))
                    .orElse((MutableComponent) CUSTOM_WORLD_DESCRIPTION);
        }

        public boolean isAmplified() {
            return Optional.ofNullable(this.preset).flatMap(Holder::unwrapKey).filter(resourceKey -> resourceKey.equals(WorldPresets.AMPLIFIED)).isPresent();
        }
    }
}