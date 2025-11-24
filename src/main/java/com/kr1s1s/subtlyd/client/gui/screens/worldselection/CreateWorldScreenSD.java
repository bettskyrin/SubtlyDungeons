package com.kr1s1s.subtlyd.client.gui.screens.worldselection;


import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.client.gui.components.GameTabButton;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.layouts.*;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.gui.screens.worldselection.*;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.commands.Commands;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.util.FileUtil;
import net.minecraft.util.Util;
import net.minecraft.world.Difficulty;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.gamerules.GameRuleMap;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPresets;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.WorldData;
import net.minecraft.world.level.validation.DirectoryValidator;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class CreateWorldScreenSD extends Screen {
    private static final int GROUP_BOTTOM = 1;
    private static final int TAB_COLUMN_WIDTH = 210;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String TEMP_WORLD_PREFIX = "mcworld-";
    static final Component GAME_MODEL_LABEL = Component.translatable("selectWorld.gameMode");
    static final Component NAME_LABEL = Component.translatable("selectWorld.enterName");
    static final Component EXPERIMENTS_LABEL = Component.translatable("selectWorld.experiments");
    static final Component ALLOW_COMMANDS_INFO = Component.translatable("selectWorld.allowCommands.info");
    private static final Component PREPARING_WORLD_DATA = Component.translatable("createWorld.preparing");
    private static final int HORIZONTAL_BUTTON_SPACING = 10;
    private static final int VERTICAL_BUTTON_SPACING = 8;
    public static final Identifier TAB_HEADER_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/tab_header_background.png");
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    final WorldCreationUiStateSD uiState;
    private final TabManager tabManager = new TabManager(guiEventListener -> {
        AbstractWidget var10000 = this.addRenderableWidget(guiEventListener);
    }, guiEventListener -> this.removeWidget(guiEventListener));
    private boolean recreated;
    private final DirectoryValidator packValidator;
    private final CreateWorldCallbackSD createWorldCallback;
    private final Runnable onClose;
    @Nullable
    private Path tempDataPackDir;
    @Nullable
    private PackRepository tempDataPackRepository;
    @Nullable
    private TabNavigationBar tabNavigationBar;

    public static void openFresh(Minecraft minecraft, Runnable runnable) {
        openFresh(
                minecraft,
                runnable,
                (createWorldScreen, layeredRegistryAccess, primaryLevelData, path) -> createWorldScreen.createNewWorld(layeredRegistryAccess, primaryLevelData)
        );
    }

    public static void openFresh(Minecraft minecraft, Runnable runnable, CreateWorldCallbackSD createWorldCallback) {
        WorldCreationContextMapperSD worldCreationContextMapper = (reloadableServerResources, layeredRegistryAccess, dataPackReloadCookie) -> new WorldCreationContextSD(
                dataPackReloadCookie.worldGenSettings(), layeredRegistryAccess, reloadableServerResources, dataPackReloadCookie.dataConfiguration()
        );
        Function<WorldLoader.DataLoadContext, WorldGenSettings> function = dataLoadContext -> new WorldGenSettings(
                WorldOptions.defaultWithRandomSeed(), WorldPresets.createNormalWorldDimensions(dataLoadContext.datapackWorldgen())
        );
        openCreateWorldScreen(minecraft, runnable, function, worldCreationContextMapper, WorldPresets.NORMAL, createWorldCallback);
    }

    public static void testWorld(Minecraft minecraft, Runnable runnable) {
        WorldCreationContextMapperSD worldCreationContextMapper = (reloadableServerResources, layeredRegistryAccess, dataPackReloadCookie) -> new WorldCreationContextSD(
                dataPackReloadCookie.worldGenSettings().options(),
                dataPackReloadCookie.worldGenSettings().dimensions(),
                layeredRegistryAccess,
                reloadableServerResources,
                dataPackReloadCookie.dataConfiguration(),
                new InitialWorldCreationOptionsSD(
                        WorldCreationUiStateSD.SelectedGameMode.CREATIVE,
                        new GameRuleMap.Builder().set(GameRules.ADVANCE_TIME, false).set(GameRules.ADVANCE_WEATHER, false).set(GameRules.SPAWN_MOBS, false).build(),
                        FlatLevelGeneratorPresets.REDSTONE_READY
                )
        );
        Function<WorldLoader.DataLoadContext, WorldGenSettings> function = dataLoadContext -> new WorldGenSettings(
                WorldOptions.testWorldWithRandomSeed(), WorldPresets.createFlatWorldDimensions(dataLoadContext.datapackWorldgen())
        );
        openCreateWorldScreen(
                minecraft,
                runnable,
                function,
                worldCreationContextMapper,
                WorldPresets.FLAT,
                (createWorldScreen, layeredRegistryAccess, primaryLevelData, path) -> createWorldScreen.createNewWorld(layeredRegistryAccess, primaryLevelData)
        );
    }

    private static void openCreateWorldScreen(
            Minecraft minecraft,
            Runnable runnable,
            Function<WorldLoader.DataLoadContext, WorldGenSettings> function,
            WorldCreationContextMapperSD worldCreationContextMapper,
            ResourceKey<WorldPreset> resourceKey,
            CreateWorldCallbackSD createWorldCallback
    ) {
        queueLoadScreen(minecraft, PREPARING_WORLD_DATA);
        PackRepository packRepository = new PackRepository(new ServerPacksSource(minecraft.directoryValidator()));
        WorldDataConfiguration worldDataConfiguration = SharedConstants.IS_RUNNING_IN_IDE
                ? new WorldDataConfiguration(new DataPackConfig(List.of("vanilla", "tests"), List.of()), FeatureFlags.DEFAULT_FLAGS)
                : WorldDataConfiguration.DEFAULT;
        WorldLoader.InitConfig initConfig = createDefaultLoadConfig(packRepository, worldDataConfiguration);
        CompletableFuture<WorldCreationContextSD> completableFuture = WorldLoader.load(
                initConfig,
                dataLoadContext -> new WorldLoader.DataLoadOutput<>(
                        new DataPackReloadCookie((WorldGenSettings)function.apply(dataLoadContext), dataLoadContext.dataConfiguration()), dataLoadContext.datapackDimensions()
                ),
                (closeableResourceManager, reloadableServerResources, layeredRegistryAccess, dataPackReloadCookie) -> {
                    closeableResourceManager.close();
                    return worldCreationContextMapper.apply(reloadableServerResources, layeredRegistryAccess, dataPackReloadCookie);
                },
                Util.backgroundExecutor(),
                minecraft
        );
        minecraft.managedBlock(completableFuture::isDone);
        minecraft.setScreen(
                new CreateWorldScreenSD(
                        minecraft, runnable, completableFuture.join(), Optional.of(resourceKey), OptionalLong.empty(), createWorldCallback
                )
        );
    }

    public static CreateWorldScreenSD createFromExisting(
            Minecraft minecraft, Runnable runnable, LevelSettings levelSettings, WorldCreationContextSD worldCreationContext, @Nullable Path path
    ) {
        CreateWorldScreenSD createWorldScreen = new CreateWorldScreenSD(
                minecraft,
                runnable,
                worldCreationContext,
                WorldPresets.fromSettings(worldCreationContext.selectedDimensions()),
                OptionalLong.of(worldCreationContext.options().seed()),
                (createWorldScreenx, layeredRegistryAccess, primaryLevelData, pathx) -> createWorldScreenx.createNewWorld(layeredRegistryAccess, primaryLevelData)
        );
        createWorldScreen.recreated = true;
        createWorldScreen.uiState.setName(levelSettings.levelName());
        createWorldScreen.uiState.setAllowCommands(levelSettings.allowCommands());
        createWorldScreen.uiState.setDifficulty(levelSettings.difficulty());
        createWorldScreen.uiState.getGameRules().setAll(levelSettings.gameRules(), null);
        if (levelSettings.hardcore()) {
            createWorldScreen.uiState.setGameMode(WorldCreationUiStateSD.SelectedGameMode.HARDCORE);
        } else if (levelSettings.gameType().isSurvival()) {
            createWorldScreen.uiState.setGameMode(WorldCreationUiStateSD.SelectedGameMode.SURVIVAL);
        } else if (levelSettings.gameType().isCreative()) {
            createWorldScreen.uiState.setGameMode(WorldCreationUiStateSD.SelectedGameMode.CREATIVE);
        }

        createWorldScreen.tempDataPackDir = path;
        return createWorldScreen;
    }

    private CreateWorldScreenSD(
            Minecraft minecraft,
            Runnable runnable,
            WorldCreationContextSD worldCreationContext,
            Optional<ResourceKey<WorldPreset>> optional,
            OptionalLong optionalLong,
            CreateWorldCallbackSD createWorldCallback
    ) {
        super(Component.translatable("selectWorld.create"));
        this.onClose = runnable;
        this.packValidator = minecraft.directoryValidator();
        this.createWorldCallback = createWorldCallback;
        this.uiState = new WorldCreationUiStateSD(minecraft.getLevelSource().getBaseDir(), worldCreationContext, optional, optionalLong);
    }

    public WorldCreationUiStateSD getUiState() {
        return this.uiState;
    }

    @Override
    protected void init() {
        this.tabNavigationBar = TabNavigationBar.builder(this.tabManager, this.width)
                .addTabs(new CreateWorldScreenSD.GameTab(), new CreateWorldScreenSD.WorldTab(), new CreateWorldScreenSD.MoreTab())
                .build();
        this.addRenderableWidget(this.tabNavigationBar);
        LinearLayout linearLayout = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
        linearLayout.addChild(Button.builder(Component.translatable("selectWorld.create"), button -> this.onCreate()).build());
        linearLayout.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> this.popScreen()).build());
        this.layout.visitWidgets(abstractWidget -> {
            abstractWidget.setTabOrderGroup(1);
            this.addRenderableWidget(abstractWidget);
        });
        this.tabNavigationBar.selectTab(0, false);
        this.uiState.onChanged();
        this.repositionElements();
    }

    @Override
    protected void setInitialFocus() {
    }

    @Override
    public void repositionElements() {
        if (this.tabNavigationBar != null) {
            this.tabNavigationBar.setWidth(this.width);
            this.tabNavigationBar.arrangeElements();
            int i = this.tabNavigationBar.getRectangle().bottom();
            ScreenRectangle screenRectangle = new ScreenRectangle(0, i, this.width, this.height - this.layout.getFooterHeight() - i);
            this.tabManager.setTabArea(screenRectangle);
            this.layout.setHeaderHeight(i);
            this.layout.arrangeElements();
        }
    }

    private static void queueLoadScreen(Minecraft minecraft, Component component) {
        minecraft.setScreenAndShow(new GenericMessageScreen(component));
    }

    private void onCreate() {
        WorldCreationContextSD worldCreationContext = this.uiState.getSettings();
        WorldDimensions.Complete complete = worldCreationContext.selectedDimensions().bake(worldCreationContext.datapackDimensions());
        LayeredRegistryAccess<RegistryLayer> layeredRegistryAccess = worldCreationContext.worldgenRegistries()
                .replaceFrom(RegistryLayer.DIMENSIONS, complete.dimensionsRegistryAccess());
        Lifecycle lifecycle = FeatureFlags.isExperimental(worldCreationContext.dataConfiguration().enabledFeatures()) ? Lifecycle.experimental() : Lifecycle.stable();
        Lifecycle lifecycle2 = layeredRegistryAccess.compositeAccess().allRegistriesLifecycle();
        Lifecycle lifecycle3 = lifecycle2.add(lifecycle);
        boolean bl = !this.recreated && lifecycle2 == Lifecycle.stable();
        LevelSettings levelSettings = this.createLevelSettings(complete.specialWorldProperty() == PrimaryLevelData.SpecialWorldProperty.DEBUG);
        PrimaryLevelData primaryLevelData = new PrimaryLevelData(levelSettings, this.uiState.getSettings().options(), complete.specialWorldProperty(), lifecycle3);
        WorldOpenFlowsSD.confirmWorldCreation(this.minecraft, this, lifecycle3, () -> this.createWorldAndCleanup(layeredRegistryAccess, primaryLevelData), bl);
    }

    private void createWorldAndCleanup(LayeredRegistryAccess<RegistryLayer> layeredRegistryAccess, PrimaryLevelData primaryLevelData) {
        boolean bl = this.createWorldCallback.create(this, layeredRegistryAccess, primaryLevelData, this.tempDataPackDir);
        this.removeTempDataPackDir();
        if (!bl) {
            this.popScreen();
        }
    }

    private boolean createNewWorld(LayeredRegistryAccess<RegistryLayer> layeredRegistryAccess, WorldData worldData) {
        String string = this.uiState.getTargetFolder();
        WorldCreationContextSD worldCreationContext = this.uiState.getSettings();
        queueLoadScreen(this.minecraft, PREPARING_WORLD_DATA);
        Optional<LevelStorageSource.LevelStorageAccess> optional = createNewWorldDirectory(this.minecraft, string, this.tempDataPackDir);
        if (optional.isEmpty()) {
            SystemToast.onPackCopyFailure(this.minecraft, string);
            return false;
        } else {
            new WorldOpenFlowsSD(this.minecraft, this.minecraft.getLevelSource())
                    .createLevelFromExistingSettings(
                            (LevelStorageSource.LevelStorageAccess)optional.get(), worldCreationContext.dataPackResources(), layeredRegistryAccess, worldData
                    );
            return true;
        }
    }

    private LevelSettings createLevelSettings(boolean bl) {
        String string = this.uiState.getName().trim();
        if (bl) {
            GameRules gameRules = new GameRules(WorldDataConfiguration.DEFAULT.enabledFeatures());
            gameRules.set(GameRules.ADVANCE_TIME, false, null);
            return new LevelSettings(string, GameType.SPECTATOR, false, Difficulty.PEACEFUL, true, gameRules, WorldDataConfiguration.DEFAULT);
        } else {
            return new LevelSettings(
                    string,
                    this.uiState.getGameMode().gameType,
                    this.uiState.isHardcore(),
                    this.uiState.getDifficulty(),
                    this.uiState.isAllowCommands(),
                    this.uiState.getGameRules(),
                    this.uiState.getSettings().dataConfiguration()
            );
        }
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (this.tabNavigationBar.keyPressed(keyEvent)) {
            return true;
        } else if (super.keyPressed(keyEvent)) {
            return true;
        } else if (keyEvent.isConfirmation()) {
            this.onCreate();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClose() {
        this.popScreen();
    }

    public void popScreen() {
        this.onClose.run();
        this.removeTempDataPackDir();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR, 0, this.height - this.layout.getFooterHeight() - 2, 0.0F, 0.0F, this.width, 2, 32, 2);
    }

    @Override
    protected void renderMenuBackground(GuiGraphics guiGraphics) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TAB_HEADER_BACKGROUND, 0, 0, 0.0F, 0.0F, this.width, this.layout.getHeaderHeight(), 16, 16);
        this.renderMenuBackground(guiGraphics, 0, this.layout.getHeaderHeight(), this.width, this.height);
    }

    @Nullable
    private Path getOrCreateTempDataPackDir() {
        if (this.tempDataPackDir == null) {
            try {
                this.tempDataPackDir = Files.createTempDirectory("mcworld-");
            } catch (IOException var2) {
                LOGGER.warn("Failed to create temporary dir", (Throwable)var2);
                SystemToast.onPackCopyFailure(this.minecraft, this.uiState.getTargetFolder());
                this.popScreen();
            }
        }

        return this.tempDataPackDir;
    }

    void openExperimentsScreen(WorldDataConfiguration worldDataConfiguration) {
        Pair<Path, PackRepository> pair = this.getDataPackSelectionSettings(worldDataConfiguration);
        if (pair != null) {
            this.minecraft
                    .setScreen(new ExperimentsScreen(this, pair.getSecond(), packRepository -> this.tryApplyNewDataPacks(packRepository, false, this::openExperimentsScreen)));
        }
    }

    void openDataPackSelectionScreen(WorldDataConfiguration worldDataConfiguration) {
        Pair<Path, PackRepository> pair = this.getDataPackSelectionSettings(worldDataConfiguration);
        if (pair != null) {
            this.minecraft
                    .setScreen(
                            new PackSelectionScreen(
                                    pair.getSecond(),
                                    packRepository -> this.tryApplyNewDataPacks(packRepository, true, this::openDataPackSelectionScreen),
                                    pair.getFirst(),
                                    Component.translatable("dataPack.title")
                            )
                    );
        }
    }

    private void tryApplyNewDataPacks(PackRepository packRepository, boolean bl, Consumer<WorldDataConfiguration> consumer) {
        List<String> list = ImmutableList.copyOf(packRepository.getSelectedIds());
        List<String> list2 = (List<String>)packRepository.getAvailableIds()
                .stream()
                .filter(string -> !list.contains(string))
                .collect(ImmutableList.toImmutableList());
        WorldDataConfiguration worldDataConfiguration = new WorldDataConfiguration(
                new DataPackConfig(list, list2), this.uiState.getSettings().dataConfiguration().enabledFeatures()
        );
        if (this.uiState.tryUpdateDataConfiguration(worldDataConfiguration)) {
            this.minecraft.setScreen(this);
        } else {
            FeatureFlagSet featureFlagSet = packRepository.getRequestedFeatureFlags();
            if (FeatureFlags.isExperimental(featureFlagSet) && bl) {
                this.minecraft.setScreen(new ConfirmExperimentalFeaturesScreen(packRepository.getSelectedPacks(), blx -> {
                    if (blx) {
                        this.applyNewPackConfig(packRepository, worldDataConfiguration, consumer);
                    } else {
                        consumer.accept(this.uiState.getSettings().dataConfiguration());
                    }
                }));
            } else {
                this.applyNewPackConfig(packRepository, worldDataConfiguration, consumer);
            }
        }
    }

    private void applyNewPackConfig(PackRepository packRepository, WorldDataConfiguration worldDataConfiguration, Consumer<WorldDataConfiguration> consumer) {
        this.minecraft.setScreenAndShow(new GenericMessageScreen(Component.translatable("dataPack.validation.working")));
        WorldLoader.InitConfig initConfig = createDefaultLoadConfig(packRepository, worldDataConfiguration);
        WorldLoader.load(
                        initConfig,
                        dataLoadContext -> {
                            if (dataLoadContext.datapackWorldgen().lookupOrThrow(Registries.WORLD_PRESET).listElements().findAny().isEmpty()) {
                                throw new IllegalStateException("Needs at least one world preset to continue");
                            } else if (dataLoadContext.datapackWorldgen().lookupOrThrow(Registries.BIOME).listElements().findAny().isEmpty()) {
                                throw new IllegalStateException("Needs at least one biome continue");
                            } else {
                                WorldCreationContextSD worldCreationContext = this.uiState.getSettings();
                                DynamicOps<JsonElement> dynamicOps = worldCreationContext.worldgenLoadContext().createSerializationContext(JsonOps.INSTANCE);
                                DataResult<JsonElement> dataResult = WorldGenSettings.encode(dynamicOps, worldCreationContext.options(), worldCreationContext.selectedDimensions())
                                        .setLifecycle(Lifecycle.stable());
                                DynamicOps<JsonElement> dynamicOps2 = dataLoadContext.datapackWorldgen().createSerializationContext(JsonOps.INSTANCE);
                                WorldGenSettings worldGenSettings = dataResult.flatMap(jsonElement -> WorldGenSettings.CODEC.parse(dynamicOps2, jsonElement))
                                        .getOrThrow(string -> new IllegalStateException("Error parsing worldgen settings after loading data packs: " + string));
                                return new WorldLoader.DataLoadOutput<>(
                                        new DataPackReloadCookie(worldGenSettings, dataLoadContext.dataConfiguration()), dataLoadContext.datapackDimensions()
                                );
                            }
                        },
                        (closeableResourceManager, reloadableServerResources, layeredRegistryAccess, dataPackReloadCookie) -> {
                            closeableResourceManager.close();
                            return new WorldCreationContextSD(
                                    dataPackReloadCookie.worldGenSettings(), layeredRegistryAccess, reloadableServerResources, dataPackReloadCookie.dataConfiguration()
                            );
                        },
                        Util.backgroundExecutor(),
                        this.minecraft
                )
                .thenApply(worldCreationContext -> {
                    worldCreationContext.validate();
                    return worldCreationContext;
                })
                .thenAcceptAsync(this.uiState::setSettings, this.minecraft)
                .handleAsync(
                        (void_, throwable) -> {
                            if (throwable != null) {
                                LOGGER.warn("Failed to validate datapack", throwable);
                                this.minecraft
                                        .setScreen(
                                                new ConfirmScreen(
                                                        bl -> {
                                                            if (bl) {
                                                                consumer.accept(this.uiState.getSettings().dataConfiguration());
                                                            } else {
                                                                consumer.accept(WorldDataConfiguration.DEFAULT);
                                                            }
                                                        },
                                                        Component.translatable("dataPack.validation.failed"),
                                                        CommonComponents.EMPTY,
                                                        Component.translatable("dataPack.validation.back"),
                                                        Component.translatable("dataPack.validation.reset")
                                                )
                                        );
                            } else {
                                this.minecraft.setScreen(this);
                            }

                            return null;
                        },
                        this.minecraft
                );
    }

    private static WorldLoader.InitConfig createDefaultLoadConfig(PackRepository packRepository, WorldDataConfiguration worldDataConfiguration) {
        WorldLoader.PackConfig packConfig = new WorldLoader.PackConfig(packRepository, worldDataConfiguration, false, true);
        return new WorldLoader.InitConfig(packConfig, Commands.CommandSelection.INTEGRATED, LevelBasedPermissionSet.GAMEMASTER);
    }

    private void removeTempDataPackDir() {
        if (this.tempDataPackDir != null && Files.exists(this.tempDataPackDir, new LinkOption[0])) {
            try {
                Stream<Path> stream = Files.walk(this.tempDataPackDir);

                try {
                    stream.sorted(Comparator.reverseOrder()).forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException var2) {
                            LOGGER.warn("Failed to remove temporary file {}", path, var2);
                        }
                    });
                } catch (Throwable var5) {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (Throwable var4) {
                            var5.addSuppressed(var4);
                        }
                    }

                    throw var5;
                }

                if (stream != null) {
                    stream.close();
                }
            } catch (IOException var6) {
                LOGGER.warn("Failed to list temporary dir {}", this.tempDataPackDir);
            }
        }

        this.tempDataPackDir = null;
    }

    private static void copyBetweenDirs(Path path, Path path2, Path path3) {
        try {
            Util.copyBetweenDirs(path, path2, path3);
        } catch (IOException var4) {
            LOGGER.warn("Failed to copy datapack file from {} to {}", path3, path2);
            throw new UncheckedIOException(var4);
        }
    }

    private static Optional<LevelStorageSource.LevelStorageAccess> createNewWorldDirectory(Minecraft minecraft, String string, @Nullable Path path) {
        try {
            LevelStorageSource.LevelStorageAccess levelStorageAccess = minecraft.getLevelSource().createAccess(string);
            if (path == null) {
                return Optional.of(levelStorageAccess);
            }

            try {
                Stream<Path> stream = Files.walk(path);

                Optional var6;
                try {
                    Path path2 = levelStorageAccess.getLevelPath(LevelResource.DATAPACK_DIR);
                    FileUtil.createDirectoriesSafe(path2);
                    stream.filter(path2x -> !path2x.equals(path)).forEach(path3 -> copyBetweenDirs(path, path2, path3));
                    var6 = Optional.of(levelStorageAccess);
                } catch (Throwable var8) {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (Throwable var7) {
                            var8.addSuppressed(var7);
                        }
                    }

                    throw var8;
                }

                if (stream != null) {
                    stream.close();
                }

                return var6;
            } catch (UncheckedIOException | IOException var9) {
                LOGGER.warn("Failed to copy datapacks to world {}", string, var9);
                levelStorageAccess.close();
            }
        } catch (UncheckedIOException | IOException var10) {
            LOGGER.warn("Failed to create access for {}", string, var10);
        }

        return Optional.empty();
    }

    public static Path createTempDataPackDirFromExistingWorld(Path path, Minecraft minecraft) {
        MutableObject<Path> mutableObject = new MutableObject<>();

        try {
            Stream<Path> stream = Files.walk(path);

            try {
                stream.filter(path2 -> !path2.equals(path)).forEach(path2 -> {
                    Path path3 = mutableObject.get();
                    if (path3 == null) {
                        try {
                            path3 = Files.createTempDirectory("mcworld-");
                        } catch (IOException var5) {
                            LOGGER.warn("Failed to create temporary dir");
                            throw new UncheckedIOException(var5);
                        }

                        mutableObject.setValue(path3);
                    }

                    copyBetweenDirs(path, path3, path2);
                });
            } catch (Throwable var7) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }

                throw var7;
            }

            if (stream != null) {
                stream.close();
            }
        } catch (UncheckedIOException | IOException var8) {
            LOGGER.warn("Failed to copy datapacks from world {}", path, var8);
            SystemToast.onPackCopyFailure(minecraft, path.toString());
            return null;
        }

        return mutableObject.get();
    }

    @Nullable
    private Pair<Path, PackRepository> getDataPackSelectionSettings(WorldDataConfiguration worldDataConfiguration) {
        Path path = this.getOrCreateTempDataPackDir();
        if (path != null) {
            if (this.tempDataPackRepository == null) {
                this.tempDataPackRepository = ServerPacksSource.createPackRepository(path, this.packValidator);
                this.tempDataPackRepository.reload();
            }

            this.tempDataPackRepository.setSelected(worldDataConfiguration.dataPacks().getEnabled());
            return Pair.of(path, this.tempDataPackRepository);
        } else {
            return null;
        }
    }

    @Environment(EnvType.CLIENT)
    /**
     * @param linearLayout The entire content layout, excluding the header and footer
     * @param linearLayout2 The first row, containing the Game Mode and World name settings
     */
    class GameTab extends GridLayoutTab {
        private static final Component TITLE = Component.translatable("createWorld.tab.game.title");
        private static final Component ALLOW_COMMANDS = Component.translatable("selectWorld.allowCommands");
        private final EditBox nameEdit;

        GameTab() {
            super(TITLE);
            LinearLayout linearLayout2 = new LinearLayout(0, 0, LinearLayout.Orientation.HORIZONTAL).spacing(4);
            linearLayout2.defaultCellSetting();
            LinearLayout linearLayout = new LinearLayout(0, 0, LinearLayout.Orientation.VERTICAL).spacing(4);
            linearLayout.defaultCellSetting().alignVerticallyMiddle();

            this.nameEdit = new EditBox(CreateWorldScreenSD.this.font, (int) (CreateWorldScreenSD.this.width / 2.5), 20, Component.translatable("selectWorld.enterName"));
            this.nameEdit.setValue(CreateWorldScreenSD.this.uiState.getName());
            this.nameEdit.setResponder(CreateWorldScreenSD.this.uiState::setName);
            CreateWorldScreenSD.this.uiState
                    .addListener(
                            worldCreationUiState -> this.nameEdit
                                    .setTooltip(
                                            Tooltip.create(
                                                    Component.translatable("selectWorld.targetFolder", Component.literal(worldCreationUiState.getTargetFolder()).withStyle(ChatFormatting.ITALIC))
                                            )
                                    )
                    );
            CreateWorldScreenSD.this.setInitialFocus(this.nameEdit);
            linearLayout2.addChild(
                    CommonLayouts.labeledElement(CreateWorldScreenSD.this.font, this.nameEdit, CreateWorldScreenSD.NAME_LABEL),
                    linearLayout2.newCellSettings().alignHorizontallyCenter()
            );

            GameTabButton survivalButton = linearLayout2.addChild(
                    GameTabButton.builder(
                            Component.translatable("selectWorld.gameMode.survival"),
                            button-> CreateWorldScreenSD.this.uiState.setGameMode(WorldCreationUiStateSD.SelectedGameMode.SURVIVAL),
                            SubtlyDungeons.resourceLocation("textures/gui/sprites/widget/survival.png"),
                            200, 140
                    ).build()
            );
            survivalButton.setSize(100, 70);
            survivalButton.setTooltip(Tooltip.create(WorldCreationUiStateSD.SelectedGameMode.SURVIVAL.getInfo()));

            GameTabButton creativeButton = linearLayout2.addChild(
                    GameTabButton.builder(
                            Component.translatable("selectWorld.gameMode.creative"),
                            button-> CreateWorldScreenSD.this.uiState.setGameMode(WorldCreationUiStateSD.SelectedGameMode.CREATIVE),
                            SubtlyDungeons.resourceLocation("textures/gui/sprites/widget/creative.png"),
                            200, 140
                    ).build()
            );
            creativeButton.setSize(100, 70);
            creativeButton.setTooltip(Tooltip.create(WorldCreationUiStateSD.SelectedGameMode.CREATIVE.getInfo()));

            this.layout.addChild(linearLayout2, 0, 0, linearLayout.newCellSettings().alignHorizontallyCenter());

//            GridLayout.RowHelper rowHelper = this.layout.rowSpacing(8).createRowHelper(1);
//            LayoutSettings layoutSettings = rowHelper.newCellSettings();
//            this.nameEdit = new EditBox(CreateWorldScreenSD.this.font, (int) (CreateWorldScreenSD.this.width / 2.5), 20, Component.translatable("selectWorld.enterName"));
//            this.nameEdit.setValue(CreateWorldScreenSD.this.uiState.getName());
//            this.nameEdit.setResponder(CreateWorldScreenSD.this.uiState::setName);
//            CreateWorldScreenSD.this.uiState
//                    .addListener(
//                            worldCreationUiState -> this.nameEdit
//                                    .setTooltip(
//                                            Tooltip.create(
//                                                    Component.translatable("selectWorld.targetFolder", Component.literal(worldCreationUiState.getTargetFolder()).withStyle(ChatFormatting.ITALIC))
//                                            )
//                                    )
//                    );
//            CreateWorldScreenSD.this.setInitialFocus(this.nameEdit);
//            rowHelper.addChild(
//                    CommonLayouts.labeledElement(CreateWorldScreenSD.this.font, this.nameEdit, CreateWorldScreenSD.NAME_LABEL),
//                    rowHelper.newCellSettings().alignHorizontallyRight()
//            );
//            CycleButton<WorldCreationUiStateSD.SelectedGameMode> cycleButton = rowHelper.addChild(
//                    CycleButton.<WorldCreationUiStateSD.SelectedGameMode>builder(selectedGameMode -> selectedGameMode.displayName, CreateWorldScreenSD.this.uiState.getGameMode())
//                            .withValues(WorldCreationUiStateSD.SelectedGameMode.SURVIVAL, WorldCreationUiStateSD.SelectedGameMode.HARDCORE, WorldCreationUiStateSD.SelectedGameMode.CREATIVE)
//                            .create(
//                                    0, 0, 210, 20, CreateWorldScreenSD.GAME_MODEL_LABEL, (cycleButtonx, selectedGameMode) -> CreateWorldScreenSD.this.uiState.setGameMode(selectedGameMode)
//                            ),
//                    layoutSettings
//            );
//            CreateWorldScreenSD.this.uiState.addListener(worldCreationUiState -> {
//                cycleButton.setValue(worldCreationUiState.getGameMode());
//                cycleButton.active = !worldCreationUiState.isDebug();
//                cycleButton.setTooltip(Tooltip.create(worldCreationUiState.getGameMode().getInfo()));
//            });
//            CycleButton<Difficulty> cycleButton2 = rowHelper.addChild(
//                    CycleButton.builder(Difficulty::getDisplayName, CreateWorldScreenSD.this.uiState.getDifficulty())
//                            .withValues(Difficulty.values())
//                            .create(
//                                    0, 0, 210, 20, Component.translatable("options.difficulty"), (cycleButtonx, difficulty) -> CreateWorldScreenSD.this.uiState.setDifficulty(difficulty)
//                            ),
//                    layoutSettings
//            );
//            CreateWorldScreenSD.this.uiState.addListener(worldCreationUiState -> {
//                cycleButton2.setValue(CreateWorldScreenSD.this.uiState.getDifficulty());
//                cycleButton2.active = !CreateWorldScreenSD.this.uiState.isHardcore();
//                cycleButton2.setTooltip(Tooltip.create(CreateWorldScreenSD.this.uiState.getDifficulty().getInfo()));
//            });
//            CycleButton<Boolean> cycleButton3 = rowHelper.addChild(
//                    CycleButton.onOffBuilder(CreateWorldScreenSD.this.uiState.isAllowCommands())
//                            .withTooltip(boolean_ -> Tooltip.create(CreateWorldScreenSD.ALLOW_COMMANDS_INFO))
//                            .create(0, 0, 210, 20, ALLOW_COMMANDS, (cycleButtonx, boolean_) -> CreateWorldScreenSD.this.uiState.setAllowCommands(boolean_))
//            );
//            CreateWorldScreenSD.this.uiState.addListener(worldCreationUiState -> {
//                cycleButton3.setValue(CreateWorldScreenSD.this.uiState.isAllowCommands());
//                cycleButton3.active = !CreateWorldScreenSD.this.uiState.isDebug() && !CreateWorldScreenSD.this.uiState.isHardcore();
//            });
//            if (!SharedConstants.getCurrentVersion().stable()) {
//                rowHelper.addChild(
//                        Button.builder(
//                                        CreateWorldScreenSD.EXPERIMENTS_LABEL,
//                                        button -> CreateWorldScreenSD.this.openExperimentsScreen(CreateWorldScreenSD.this.uiState.getSettings().dataConfiguration())
//                                )
//                                .width(210)
//                                .build()
//                );
//            }
        }
    }

    @Environment(EnvType.CLIENT)
    class MoreTab extends GridLayoutTab {
        private static final Component TITLE = Component.translatable("createWorld.tab.more.title");
        private static final Component GAME_RULES_LABEL = Component.translatable("selectWorld.gameRules");
        private static final Component DATA_PACKS_LABEL = Component.translatable("selectWorld.dataPacks");

        MoreTab() {
            super(TITLE);
            GridLayout.RowHelper rowHelper = this.layout.rowSpacing(8).createRowHelper(1);
            rowHelper.addChild(Button.builder(GAME_RULES_LABEL, button -> this.openGameRulesScreen()).width(TAB_COLUMN_WIDTH).build());
            rowHelper.addChild(
                    Button.builder(
                                    CreateWorldScreenSD.EXPERIMENTS_LABEL,
                                    button -> CreateWorldScreenSD.this.openExperimentsScreen(CreateWorldScreenSD.this.uiState.getSettings().dataConfiguration())
                            )
                            .width(TAB_COLUMN_WIDTH)
                            .build()
            );
            rowHelper.addChild(
                    Button.builder(
                                    DATA_PACKS_LABEL, button -> CreateWorldScreenSD.this.openDataPackSelectionScreen(CreateWorldScreenSD.this.uiState.getSettings().dataConfiguration())
                            )
                            .width(TAB_COLUMN_WIDTH)
                            .build()
            );
        }

        private void openGameRulesScreen() {
            CreateWorldScreenSD.this.minecraft
                    .setScreen(
                            new EditGameRulesScreen(
                                    CreateWorldScreenSD.this.uiState.getGameRules().copy(CreateWorldScreenSD.this.uiState.getSettings().dataConfiguration().enabledFeatures()), optional -> {
                                CreateWorldScreenSD.this.minecraft.setScreen(CreateWorldScreenSD.this);
                                optional.ifPresent(CreateWorldScreenSD.this.uiState::setGameRules);
                            }
                            )
                    );
        }
    }

    @Environment(EnvType.CLIENT)
    class WorldTab extends GridLayoutTab {
        private static final Component TITLE = Component.translatable("createWorld.tab.world.title");
        private static final Component AMPLIFIED_HELP_TEXT = Component.translatable("generator.minecraft.amplified.info");
        private static final Component GENERATE_STRUCTURES = Component.translatable("selectWorld.mapFeatures");
        private static final Component GENERATE_STRUCTURES_INFO = Component.translatable("selectWorld.mapFeatures.info");
        private static final Component BONUS_CHEST = Component.translatable("selectWorld.bonusItems");
        private static final Component SEED_LABEL = Component.translatable("selectWorld.enterSeed");
        static final Component SEED_EMPTY_HINT = Component.translatable("selectWorld.seedInfo");
        private static final int WORLD_TAB_WIDTH = 310;
        private final EditBox seedEdit;
        private final Button customizeTypeButton;

        WorldTab() {
            super(TITLE);
            GridLayout.RowHelper rowHelper = this.layout.columnSpacing(10).rowSpacing(8).createRowHelper(2);
            CycleButton<WorldCreationUiStateSD.WorldTypeEntry> cycleButton = rowHelper.addChild(
                    CycleButton.builder(WorldCreationUiStateSD.WorldTypeEntry::describePreset, CreateWorldScreenSD.this.uiState.getWorldType())
                            .withValues(this.createWorldTypeValueSupplier())
                            .withCustomNarration(CreateWorldScreenSD.WorldTab::createTypeButtonNarration)
                            .create(
                                    0,
                                    0,
                                    150,
                                    20,
                                    Component.translatable("selectWorld.mapType"),
                                    (cycleButtonx, worldTypeEntry) -> CreateWorldScreenSD.this.uiState.setWorldType(worldTypeEntry)
                            )
            );
            cycleButton.setValue(CreateWorldScreenSD.this.uiState.getWorldType());
            CreateWorldScreenSD.this.uiState.addListener(worldCreationUiState -> {
                WorldCreationUiStateSD.WorldTypeEntry worldTypeEntry = worldCreationUiState.getWorldType();
                cycleButton.setValue(worldTypeEntry);
                if (worldTypeEntry.isAmplified()) {
                    cycleButton.setTooltip(Tooltip.create(AMPLIFIED_HELP_TEXT));
                } else {
                    cycleButton.setTooltip(null);
                }

                cycleButton.active = CreateWorldScreenSD.this.uiState.getWorldType().preset() != null;
            });
            this.customizeTypeButton = rowHelper.addChild(Button.builder(Component.translatable("selectWorld.customizeType"), button -> this.openPresetEditor()).build());
            CreateWorldScreenSD.this.uiState
                    .addListener(worldCreationUiState -> this.customizeTypeButton.active = !worldCreationUiState.isDebug() && worldCreationUiState.getPresetEditor() != null);
            this.seedEdit = new EditBox(CreateWorldScreenSD.this.font, 308, 20, Component.translatable("selectWorld.enterSeed")) {
                @Override
                protected MutableComponent createNarrationMessage() {
                    return super.createNarrationMessage().append(CommonComponents.NARRATION_SEPARATOR).append(CreateWorldScreenSD.WorldTab.SEED_EMPTY_HINT);
                }
            };
            this.seedEdit.setHint(SEED_EMPTY_HINT);
            this.seedEdit.setValue(CreateWorldScreenSD.this.uiState.getSeed());
            this.seedEdit.setResponder(string -> CreateWorldScreenSD.this.uiState.setSeed(this.seedEdit.getValue()));
            rowHelper.addChild(CommonLayouts.labeledElement(CreateWorldScreenSD.this.font, this.seedEdit, SEED_LABEL), 2);
            SwitchGrid.Builder builder = SwitchGrid.builder(310);
            builder.addSwitch(GENERATE_STRUCTURES, CreateWorldScreenSD.this.uiState::isGenerateStructures, CreateWorldScreenSD.this.uiState::setGenerateStructures)
                    .withIsActiveCondition(() -> !CreateWorldScreenSD.this.uiState.isDebug())
                    .withInfo(GENERATE_STRUCTURES_INFO);
            builder.addSwitch(BONUS_CHEST, CreateWorldScreenSD.this.uiState::isBonusChest, CreateWorldScreenSD.this.uiState::setBonusChest)
                    .withIsActiveCondition(() -> !CreateWorldScreenSD.this.uiState.isHardcore() && !CreateWorldScreenSD.this.uiState.isDebug());
            SwitchGrid switchGrid = builder.build();
            rowHelper.addChild(switchGrid.layout(), 2);
            CreateWorldScreenSD.this.uiState.addListener(worldCreationUiState -> switchGrid.refreshStates());
        }

        private void openPresetEditor() {
            PresetEditorSD presetEditor = CreateWorldScreenSD.this.uiState.getPresetEditor();
            if (presetEditor != null) {
                CreateWorldScreenSD.this.minecraft.setScreen(presetEditor.createEditScreen(CreateWorldScreenSD.this, CreateWorldScreenSD.this.uiState.getSettings()));
            }
        }

        private CycleButton.ValueListSupplier<WorldCreationUiStateSD.WorldTypeEntry> createWorldTypeValueSupplier() {
            return new CycleButton.ValueListSupplier<>() {
                @Override
                public List<WorldCreationUiStateSD.WorldTypeEntry> getSelectedList() {
                    return CycleButton.DEFAULT_ALT_LIST_SELECTOR.getAsBoolean()
                            ? CreateWorldScreenSD.this.uiState.getAltPresetList()
                            : CreateWorldScreenSD.this.uiState.getNormalPresetList();
                }

                @Override
                public List<WorldCreationUiStateSD.WorldTypeEntry> getDefaultList() {
                    return CreateWorldScreenSD.this.uiState.getNormalPresetList();
                }
            };
        }

        private static MutableComponent createTypeButtonNarration(CycleButton<WorldCreationUiStateSD.WorldTypeEntry> cycleButton) {
            return cycleButton.getValue().isAmplified()
                    ? CommonComponents.joinForNarration(cycleButton.createDefaultNarrationMessage(), AMPLIFIED_HELP_TEXT)
                    : cycleButton.createDefaultNarrationMessage();
        }
    }
}
