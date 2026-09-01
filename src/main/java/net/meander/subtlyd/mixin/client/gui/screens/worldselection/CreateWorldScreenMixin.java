package net.meander.subtlyd.mixin.client.gui.screens.worldselection;

import com.llamalad7.mixinextras.sugar.Local;
import net.meander.subtlyd.client.gui.components.ThumbnailButton;
import net.meander.subtlyd.client.gui.screens.CustomTerrainSettings;
import net.meander.subtlyd.client.gui.screens.CustomTerrainSettingsScreen;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.components.tabs.MenuTabBar;
import net.minecraft.client.gui.layouts.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.SwitchGrid;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.storage.LevelDataAndDimensions;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(CreateWorldScreen.class)
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public abstract class CreateWorldScreenMixin extends Screen {
    @Shadow @Final @Mutable private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    @Shadow @Final private WorldCreationUiState uiState;
    @Shadow private MenuTabBar tabNavigationBar;
    private static final boolean CAN_CHANGE_UI = Minecraft.getInstance().options.experimentalGui().get();

    protected CreateWorldScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init()V", at = @At("HEAD"))
    private void initWorldGeneration(CallbackInfo ci) {
        CustomTerrainSettings.reset();
    }

    @Inject(method = "init()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;", shift = At.Shift.AFTER), cancellable = true)
    private void init(CallbackInfo ci) {
        if (CAN_CHANGE_UI) {
            final CreateWorldScreen createWorldScreen = (CreateWorldScreen) (Object) this;
            final int ROW_SPACING = 4;
            final int BUTTON_MIDDLE_X = 100;

            GridLayout gridLayout = layout.addToFooter(new GridLayout().columnSpacing(8).rowSpacing(ROW_SPACING));

            gridLayout.defaultCellSetting().alignHorizontallyCenter();

            GridLayout.RowHelper rowHelper = gridLayout.createRowHelper(createWorldScreen.width);

            rowHelper.addChild(new SpacerElement(createWorldScreen.width / 2 - (BUTTON_MIDDLE_X - (ROW_SPACING * 3)), 0));
            rowHelper.addChild(Button.builder(Component.translatable("selectWorld.create"), _ -> createWorldScreen.onCreate()).build());
            rowHelper.addChild(new SpacerElement(createWorldScreen.width / 2 - (BUTTON_MIDDLE_X + (UtilSD.GUI_COMMON.BACK_BUTTON_WIDTH / 2) + (7 * ROW_SPACING)), 0));
            rowHelper.addChild(Button.builder(CommonComponents.GUI_CANCEL, (_) -> createWorldScreen.popScreen())
                    .width(UtilSD.GUI_COMMON.BACK_BUTTON_WIDTH)
                    .build()
            );
            layout.visitWidgets((button) -> {
                button.setTabOrderGroup(1);
                addRenderableWidget(button);
            });
            tabNavigationBar.selectTab(0, false);
            uiState.onChanged();
            repositionElements();
            ci.cancel();
        }
    }
    @Inject(method = "createNewWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows;createLevelFromExistingSettings(Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lnet/minecraft/server/ReloadableServerResources;Lnet/minecraft/core/LayeredRegistryAccess;Lnet/minecraft/world/level/storage/LevelDataAndDimensions$WorldDataAndGenSettings;Ljava/util/Optional;)V"))
    private void saveCustomTerrainData(LayeredRegistryAccess<?> finalLayers, LevelDataAndDimensions.WorldDataAndGenSettings worldDataAndGenSettings, Optional<GameRules> gameRules, CallbackInfoReturnable<Boolean> cir, @Local(name = "newWorldAccess") Optional<LevelStorageSource.LevelStorageAccess> newWorldAccess) {
        if (newWorldAccess.isPresent()) {
            Path worldRootPath = newWorldAccess.get().getLevelPath(LevelResource.ROOT);

            CustomTerrainSettings.saveSettingsToFile(worldRootPath);
        }
    }

    @Mixin(targets = "net.minecraft.client.gui.screens.worldselection.CreateWorldScreen$GameTab")
    public static class GameTabMixin extends GridLayoutTab {
        @Shadow @Final private static Component TITLE;
        @Shadow @Final private static Component ALLOW_COMMANDS;
        @Shadow @Final @Mutable private EditBox nameEdit;
        private static final Component NAME_LABEL = Component.translatable("selectWorld.enterName");
        private static final Component GAME_MODE_LABEL = Component.translatable("selectWorld.gameMode");
        private static final Component DIFFICULTY_LABEL = Component.translatable("options.difficulty");
        private static final Component ALLOW_COMMANDS_INFO = Component.translatable("selectWorld.allowCommands.info");
        private static final Component HARDCORE = Component.translatable("selectWorld.gameMode.hardcore");
        private static final Component HARDCORE_INFO = Component.translatable("selectWorld.gameMode.hardcore.info");
        private static final boolean CAN_CHANGE_UI = Minecraft.getInstance().options.experimentalGui().get();

        public GameTabMixin(EditBox nameEdit) {
            super(TITLE);

            this.nameEdit = nameEdit;
        }

        @SuppressWarnings("unchecked")
        @Inject(method = "<init>", at = @At("RETURN"))
        private void init(CreateWorldScreen helper, CallbackInfo ci) {
            if (CAN_CHANGE_UI) {
                final int SPACING = 4;
                final int GAME_MODE_BUTTON_WIDTH = 117;
                final int GAME_MODE_BUTTON_HEIGHT = 86;
                final int GAME_MODE_BUTTON_TEXTURE_WIDTH = 192;
                final int GAME_MODE_BUTTON_TEXTURE_HEIGHT = 141;
                final int DIFFICULTY_BUTTON_WIDTH = 85;
                final int DIFFICULTY_BUTTON_HEIGHT = 69;
                final int DIFFICULTY_BUTTON_TEXTURE_WIDTH = 200;
                final int DIFFICULTY_BUTTON_TEXTURE_HEIGHT = 159;
                final int WORLD_SETTINGS_WIDTH = (int) (helper.width / 2.5);

                LinearLayout screenLayout = new LinearLayout(0, 0, LinearLayout.Orientation.VERTICAL).spacing(SPACING);
                LinearLayout topRowLayout = new LinearLayout(0, 0, LinearLayout.Orientation.HORIZONTAL).spacing(SPACING);
                LinearLayout gameModeLayout = new LinearLayout(0, 0, LinearLayout.Orientation.HORIZONTAL).spacing(SPACING);
                LinearLayout worldSettingsLayout = new LinearLayout(0, 0, LinearLayout.Orientation.VERTICAL).spacing(SPACING);
                LinearLayout difficultyLayout = new LinearLayout(0, 0, LinearLayout.Orientation.HORIZONTAL).spacing(SPACING);
                SwitchGrid.Builder worldSettingsGridBuilder = SwitchGrid.builder(WORLD_SETTINGS_WIDTH);

                WorldCreationUiState uiState = helper.getUiState();

                screenLayout.defaultCellSetting().alignVerticallyMiddle();
                topRowLayout.defaultCellSetting().paddingHorizontal(SPACING);
                difficultyLayout.defaultCellSetting().alignHorizontallyLeft();
                worldSettingsLayout.defaultCellSetting().alignHorizontallyCenter();

                /* Game Mode */
                ThumbnailButton survivalButton = gameModeLayout.addChild(
                        ThumbnailButton.builder(
                                Component.translatable("selectWorld.gameMode.survival"),
                                _ -> {
                                    if (!uiState.isHardcore()) {
                                        uiState.setGameMode(WorldCreationUiState.SelectedGameMode.SURVIVAL);
                                    }
                                },
                                UtilSD.identifier("textures/gui/sprites/widget/game_mode/survival.png"),
                                UtilSD.identifier("textures/gui/sprites/widget/game_mode/survival_highlighted.png"),
                                UtilSD.identifier("textures/gui/sprites/widget/game_mode/survival_locked.png"),
                                GAME_MODE_BUTTON_TEXTURE_WIDTH, GAME_MODE_BUTTON_TEXTURE_HEIGHT
                        ).build()
                );
                survivalButton.setSize(GAME_MODE_BUTTON_WIDTH, GAME_MODE_BUTTON_HEIGHT);
                survivalButton.setTooltip(Tooltip.create(WorldCreationUiState.SelectedGameMode.SURVIVAL.getInfo()));
                survivalButton.setSelected(() -> uiState.getGameMode() == WorldCreationUiState.SelectedGameMode.SURVIVAL);

                survivalButton.active = uiState.getGameMode() != WorldCreationUiState.SelectedGameMode.HARDCORE;
                ThumbnailButton creativeButton = gameModeLayout.addChild(
                        ThumbnailButton.builder(
                                Component.translatable("selectWorld.gameMode.creative"),
                                _ -> {
                                    uiState.setGameMode(WorldCreationUiState.SelectedGameMode.CREATIVE);
                                    uiState.setAllowCommands(true);
                                },
                                UtilSD.identifier("textures/gui/sprites/widget/game_mode/creative.png"),
                                UtilSD.identifier("textures/gui/sprites/widget/game_mode/creative_highlighted.png"),
                                UtilSD.identifier("textures/gui/sprites/widget/game_mode/creative_disabled.png"),
                                GAME_MODE_BUTTON_TEXTURE_WIDTH, GAME_MODE_BUTTON_TEXTURE_HEIGHT
                        ).build()
                );

                creativeButton.setSize(GAME_MODE_BUTTON_WIDTH, GAME_MODE_BUTTON_HEIGHT);
                creativeButton.setTooltip(Tooltip.create(WorldCreationUiState.SelectedGameMode.CREATIVE.getInfo()));
                creativeButton.setSelected(() -> uiState.getGameMode() == WorldCreationUiState.SelectedGameMode.CREATIVE);

                nameEdit = new EditBox(helper.getFont(), WORLD_SETTINGS_WIDTH, 20, Component.translatable("selectWorld.enterName"));

                nameEdit.setValue(uiState.getName());
                nameEdit.setResponder(uiState::setName);
                helper.setInitialFocus(nameEdit);

                worldSettingsGridBuilder.addSwitch(HARDCORE, uiState::isHardcore, (isHardcore) -> {
                    uiState.setGameMode(isHardcore ? WorldCreationUiState.SelectedGameMode.HARDCORE : WorldCreationUiState.SelectedGameMode.SURVIVAL);
                    uiState.setDifficulty(Difficulty.HARD);

                    if (isHardcore) {
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.FLINTANDSTEEL_USE, 1.0F, 1.0F));
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.FIRECHARGE_USE, 0.8F, 0.5F));
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.FIRE_AMBIENT, 0.5F));
                    }
                }).withInfo(HARDCORE_INFO);

                worldSettingsGridBuilder.addSwitch(ALLOW_COMMANDS, uiState::isAllowCommands, uiState::setAllowCommands).withInfo(ALLOW_COMMANDS_INFO);
                worldSettingsLayout.addChild(CommonLayouts.labeledElement(helper.getFont(), nameEdit, NAME_LABEL), topRowLayout.newCellSettings().alignHorizontallyCenter());

                SwitchGrid worldSettingsGrid = worldSettingsGridBuilder.build();

                worldSettingsLayout.addChild(worldSettingsGrid.layout());
                topRowLayout.addChild(CommonLayouts.labeledElement(helper.getFont(), gameModeLayout, GAME_MODE_LABEL));
                topRowLayout.addChild(worldSettingsLayout);
                layout.addChild(topRowLayout, 0, 0, screenLayout.newCellSettings().alignHorizontallyLeft());

                /* Difficulty */
                ThumbnailButton peacefulButton = difficultyLayout.addChild(
                        ThumbnailButton.builder(
                                Component.translatable("options.difficulty.peaceful"),
                                _ -> uiState.setDifficulty(Difficulty.PEACEFUL),
                                UtilSD.identifier("textures/gui/sprites/widget/difficulty/peaceful.png"),
                                UtilSD.identifier("textures/gui/sprites/widget/difficulty/peaceful_highlighted.png"),
                                UtilSD.identifier("textures/gui/sprites/widget/difficulty/peaceful_locked.png"),
                                DIFFICULTY_BUTTON_TEXTURE_WIDTH, DIFFICULTY_BUTTON_TEXTURE_HEIGHT
                        ).build()
                );

                peacefulButton.setSize(DIFFICULTY_BUTTON_WIDTH, DIFFICULTY_BUTTON_HEIGHT);
                peacefulButton.setTooltip(Tooltip.create(Difficulty.PEACEFUL.getInfo()));
                peacefulButton.setSelected(() -> uiState.getDifficulty() == Difficulty.PEACEFUL);

                ThumbnailButton easyButton = difficultyLayout.addChild(
                        ThumbnailButton.builder(
                                Component.translatable("options.difficulty.easy"),
                                _ -> uiState.setDifficulty(Difficulty.EASY),
                                UtilSD.identifier("textures/gui/sprites/widget/difficulty/easy.png"),
                                UtilSD.identifier("textures/gui/sprites/widget/difficulty/easy_highlighted.png"),
                                UtilSD.identifier("textures/gui/sprites/widget/difficulty/easy_locked.png"),
                                DIFFICULTY_BUTTON_TEXTURE_WIDTH, DIFFICULTY_BUTTON_TEXTURE_HEIGHT
                        ).build()
                );

                easyButton.setSize(DIFFICULTY_BUTTON_WIDTH, DIFFICULTY_BUTTON_HEIGHT);
                easyButton.setTooltip(Tooltip.create(Difficulty.EASY.getInfo()));
                easyButton.setSelected(() -> uiState.getDifficulty() == Difficulty.EASY);

                ThumbnailButton normalButton = difficultyLayout.addChild(
                        ThumbnailButton.builder(
                                Component.translatable("options.difficulty.normal"),
                                _ -> uiState.setDifficulty(Difficulty.NORMAL),
                                UtilSD.identifier("textures/gui/sprites/widget/difficulty/normal.png"),
                                UtilSD.identifier("textures/gui/sprites/widget/difficulty/normal_highlighted.png"),
                                UtilSD.identifier("textures/gui/sprites/widget/difficulty/normal_locked.png"),
                                DIFFICULTY_BUTTON_TEXTURE_WIDTH, DIFFICULTY_BUTTON_TEXTURE_HEIGHT
                        ).build()
                );

                normalButton.setSize(DIFFICULTY_BUTTON_WIDTH, DIFFICULTY_BUTTON_HEIGHT);
                normalButton.setTooltip(Tooltip.create(Difficulty.NORMAL.getInfo()));
                normalButton.setSelected(() -> uiState.getDifficulty() == Difficulty.NORMAL);

                ThumbnailButton hardButton = difficultyLayout.addChild(
                        ThumbnailButton.builder(
                                Component.translatable("options.difficulty.hard"),
                                _ -> uiState.setDifficulty(Difficulty.HARD),
                                UtilSD.identifier("textures/gui/sprites/widget/difficulty/hard.png"),
                                UtilSD.identifier("textures/gui/sprites/widget/difficulty/hard_highlighted.png"),
                                UtilSD.identifier("textures/gui/sprites/widget/difficulty/hard_highlighted.png"),
                                DIFFICULTY_BUTTON_TEXTURE_WIDTH, DIFFICULTY_BUTTON_TEXTURE_HEIGHT
                        ).build()
                );

                hardButton.setSize(DIFFICULTY_BUTTON_WIDTH, DIFFICULTY_BUTTON_HEIGHT);
                hardButton.setTooltip(Tooltip.create(Difficulty.HARD.getInfo()));
                hardButton.setSelected(() -> uiState.getDifficulty() == Difficulty.HARD);

                uiState.addListener(worldCreationUiState -> {
                    int widgetIndex = 0;
                    List<AbstractWidget> worldButtons = new ArrayList<>();

                    nameEdit.setTooltip(Tooltip.create(Component.translatable("selectWorld.targetFolder",
                            Component.literal(worldCreationUiState.getTargetFolder()).withStyle(ChatFormatting.ITALIC))));
                    survivalButton.setLocked(uiState.isHardcore());
                    creativeButton.setActive(!uiState.isHardcore());
                    peacefulButton.setActive(!uiState.isHardcore());
                    easyButton.setActive(!uiState.isHardcore());
                    normalButton.setActive(!uiState.isHardcore());
                    hardButton.setLocked(uiState.isHardcore());
                    worldSettingsGrid.layout().visitWidgets(worldButtons::add);

                    for (AbstractWidget widget : worldButtons) {
                        if (widget instanceof CycleButton<?> button) {
                            if (widgetIndex == 1) {
                                ((CycleButton<Boolean>) button).setValue(uiState.isAllowCommands());
                                break;
                            }

                            widgetIndex++;
                        }
                    }
                });

                LayoutElement bottomRow = CommonLayouts.labeledElement(helper.getFont(), difficultyLayout, DIFFICULTY_LABEL);

                layout.addChild(bottomRow, 1, 0, screenLayout.newCellSettings().alignHorizontallyLeft().paddingHorizontal(SPACING));
            }
        }

        @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;Lnet/minecraft/client/gui/layouts/LayoutSettings;)Lnet/minecraft/client/gui/layouts/LayoutElement;"))
        private LayoutElement cancelWidgets(GridLayout.RowHelper instance, LayoutElement widget, LayoutSettings layoutSettings) {
            if (CAN_CHANGE_UI) {
                return widget;
            }

            return instance.addChild(widget, layoutSettings);
        }

        @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;"), require = 0)
        private LayoutElement cancelWidgets(GridLayout.RowHelper instance, LayoutElement widget) {
            if (CAN_CHANGE_UI) {
                return widget;
            }

            return instance.addChild(widget);
        }
    }

    @Mixin(targets = "net.minecraft.client.gui.screens.worldselection.CreateWorldScreen$WorldTab")
    public static class WorldTabMixin extends GridLayoutTab {
        @Shadow @Final private Button customizeTypeButton;
        @Shadow @Final CreateWorldScreen this$0;

        public WorldTabMixin(Component title) {
            super(title);
        }

        @Inject(method = "<init>", at = @At("RETURN"))
        private void addCustomizeButton(CreateWorldScreen helper, CallbackInfo ci) {
            helper.getUiState().addListener(data -> {
                Holder<WorldPreset> worldPreset = data.getWorldType().preset();

                if (worldPreset != null && worldPreset.is(WorldPresets.NORMAL)) {
                    customizeTypeButton.active = true;
                }
            });
        }

        @Inject(method = "openPresetEditor", at = @At("HEAD"), cancellable = true)
        private void openWorldValueSliderScreen(CallbackInfo ci) {
            CreateWorldScreen parentScreen = this$0;

            if (parentScreen != null) {
                Holder<WorldPreset> worldPreset = parentScreen.getUiState().getWorldType().preset();

                if (worldPreset != null && worldPreset.is(WorldPresets.NORMAL)) {
                    Minecraft.getInstance().setScreenAndShow(new CustomTerrainSettingsScreen(parentScreen));
                    ci.cancel();
                }
            }
        }
    }
}
