package net.meander.subtlyd.mixin.client.gui.screens.worldselection;

import net.meander.subtlyd.client.OptionInstanceSD;
import net.meander.subtlyd.client.gui.components.GameTabButton;
import net.meander.subtlyd.util.Util;
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
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin extends Screen {
    @Shadow @Final @Mutable private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    @Shadow @Final private WorldCreationUiState uiState;
    @Shadow private MenuTabBar tabNavigationBar;
    private static final boolean canChangeUi = OptionInstanceSD.EXPERIMENTAL_GUI.get();

    protected CreateWorldScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init()V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;",
                    shift = At.Shift.AFTER), cancellable = true)
    private void init(CallbackInfo ci) {
        if (canChangeUi) {
            final CreateWorldScreen createWorldScreen = (CreateWorldScreen) (Object) this;
            final int ROW_SPACING = 4;
            final int BUTTON_MIDDLE_X = 100;

            GridLayout gridLayout = this.layout.addToFooter(new GridLayout().columnSpacing(8).rowSpacing(ROW_SPACING));
            gridLayout.defaultCellSetting().alignHorizontallyCenter();
            GridLayout.RowHelper rowHelper = gridLayout.createRowHelper(createWorldScreen.width);
            rowHelper.addChild(new SpacerElement(createWorldScreen.width / 2 - (BUTTON_MIDDLE_X - (ROW_SPACING * 3)), 0));
            rowHelper.addChild(
                    Button.builder(Component.translatable("selectWorld.create"), _ -> createWorldScreen.onCreate())
                            .build()
            );

            rowHelper.addChild(new SpacerElement(createWorldScreen.width / 2 - (BUTTON_MIDDLE_X + (Util.GUI_COMMON.BACK_BUTTON_WIDTH / 2) + (7 * ROW_SPACING)), 0));

            rowHelper.addChild(
                    Button.builder(
                                    CommonComponents.GUI_CANCEL, (_) -> createWorldScreen.popScreen())
                            .width(Util.GUI_COMMON.BACK_BUTTON_WIDTH)
                            .build());
            this.layout.visitWidgets((button) -> {
                button.setTabOrderGroup(1);
                this.addRenderableWidget(button);
            });
            tabNavigationBar.selectTab(0, false);
            uiState.onChanged();
            this.repositionElements();
            ci.cancel();
        }
    }

    /**
     * Changes the Game Tab Layout
     */
    @Mixin(targets = "net.minecraft.client.gui.screens.worldselection.CreateWorldScreen$GameTab")
    public static class GameTabMixin extends GridLayoutTab {
        @Shadow @Final @Mutable private EditBox nameEdit;
        private static final Component TITLE = Component.translatable("createWorld.tab.game.title");
        private static final Component NAME_LABEL = Component.translatable("selectWorld.enterName");
        private static final Component GAME_MODE_LABEL = Component.translatable("selectWorld.gameMode");
        private static final Component DIFFICULTY_LABEL = Component.translatable("options.difficulty");
        private static final Component ALLOW_COMMANDS = Component.translatable("selectWorld.allowCommands");
        private static final Component ALLOW_COMMANDS_INFO = Component.translatable("selectWorld.allowCommands.info");
        private static final Component HARDCORE = Component.translatable("selectWorld.gameMode.hardcore");
        private static final Component HARDCORE_INFO = Component.translatable("selectWorld.gameMode.hardcore.info");
        private static final boolean canChangeUi = OptionInstanceSD.EXPERIMENTAL_GUI.get();

        public GameTabMixin(EditBox nameEdit) {
            super(TITLE);
            this.nameEdit = nameEdit;
        }

        @SuppressWarnings("unchecked")
        @Inject(method = "<init>", at = @At("RETURN"))
        private void init(CreateWorldScreen helper, CallbackInfo ci) {
            if (canChangeUi) {
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
                LinearLayout linearLayout = new LinearLayout(0, 0, LinearLayout.Orientation.VERTICAL).spacing(SPACING);
                LinearLayout topRow = new LinearLayout(0, 0, LinearLayout.Orientation.HORIZONTAL).spacing(SPACING);
                LinearLayout gameModeSection = new LinearLayout(0, 0, LinearLayout.Orientation.HORIZONTAL).spacing(SPACING);
                LinearLayout worldSettingsSection = new LinearLayout(0, 0, LinearLayout.Orientation.VERTICAL).spacing(SPACING);
                LinearLayout difficultySection = new LinearLayout(0, 0, LinearLayout.Orientation.HORIZONTAL).spacing(SPACING);
                SwitchGrid.Builder switchGridBuilder = SwitchGrid.builder(WORLD_SETTINGS_WIDTH);
                WorldCreationUiState uiState = helper.getUiState();

                linearLayout.defaultCellSetting().alignVerticallyMiddle();
                topRow.defaultCellSetting().paddingHorizontal(SPACING);
                difficultySection.defaultCellSetting().alignHorizontallyLeft();
                worldSettingsSection.defaultCellSetting().alignHorizontallyCenter();

                /* Game Mode */
                GameTabButton survivalButton = gameModeSection.addChild(
                        GameTabButton.builder(
                                Component.translatable("selectWorld.gameMode.survival"),
                                _ -> {
                                    if (!uiState.isHardcore()) {
                                        uiState.setGameMode(WorldCreationUiState.SelectedGameMode.SURVIVAL);
                                    }
                                },
                                Util.identifier("textures/gui/sprites/widget/game_mode/survival.png"),
                                Util.identifier("textures/gui/sprites/widget/game_mode/survival_highlighted.png"),
                                Util.identifier("textures/gui/sprites/widget/game_mode/survival_locked.png"),
                                GAME_MODE_BUTTON_TEXTURE_WIDTH, GAME_MODE_BUTTON_TEXTURE_HEIGHT
                        ).build()
                );
                survivalButton.setSize(GAME_MODE_BUTTON_WIDTH, GAME_MODE_BUTTON_HEIGHT);
                survivalButton.setTooltip(Tooltip.create(WorldCreationUiState.SelectedGameMode.SURVIVAL.getInfo()));
                survivalButton.setSelected(() -> uiState.getGameMode() == WorldCreationUiState.SelectedGameMode.SURVIVAL);
                survivalButton.active = uiState.getGameMode() != WorldCreationUiState.SelectedGameMode.HARDCORE;

                GameTabButton creativeButton = gameModeSection.addChild(
                        GameTabButton.builder(
                                Component.translatable("selectWorld.gameMode.creative"),
                                _ -> {
                                    uiState.setGameMode(WorldCreationUiState.SelectedGameMode.CREATIVE);
                                    uiState.setAllowCommands(true);
                                },
                                Util.identifier("textures/gui/sprites/widget/game_mode/creative.png"),
                                Util.identifier("textures/gui/sprites/widget/game_mode/creative_highlighted.png"),
                                Util.identifier("textures/gui/sprites/widget/game_mode/creative_disabled.png"),
                                GAME_MODE_BUTTON_TEXTURE_WIDTH, GAME_MODE_BUTTON_TEXTURE_HEIGHT
                        ).build()
                );
                creativeButton.setSize(GAME_MODE_BUTTON_WIDTH, GAME_MODE_BUTTON_HEIGHT);
                creativeButton.setTooltip(Tooltip.create(WorldCreationUiState.SelectedGameMode.CREATIVE.getInfo()));
                creativeButton.setSelected(() -> uiState.getGameMode() == WorldCreationUiState.SelectedGameMode.CREATIVE);

                this.nameEdit = new EditBox(helper.getFont(), WORLD_SETTINGS_WIDTH, 20, Component.translatable("selectWorld.enterName"));
                this.nameEdit.setValue(uiState.getName());
                this.nameEdit.setResponder(uiState::setName);
                helper.setInitialFocus(this.nameEdit);

                switchGridBuilder.addSwitch(HARDCORE, uiState::isHardcore, (isHardcore) -> {
                    uiState.setGameMode(isHardcore ? WorldCreationUiState.SelectedGameMode.HARDCORE : WorldCreationUiState.SelectedGameMode.SURVIVAL);
                    uiState.setDifficulty(Difficulty.HARD);
                    if (isHardcore) {
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.FLINTANDSTEEL_USE, 1.0F, 1.0F));
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.FIRECHARGE_USE, 0.8F, 0.5F));
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.FIRE_AMBIENT, 0.5F));
                    }
                }).withInfo(HARDCORE_INFO);

                switchGridBuilder.addSwitch(ALLOW_COMMANDS, uiState::isAllowCommands, uiState::setAllowCommands).withInfo(ALLOW_COMMANDS_INFO);
                worldSettingsSection.addChild(
                        CommonLayouts.labeledElement(helper.getFont(), this.nameEdit, NAME_LABEL),
                        topRow.newCellSettings().alignHorizontallyCenter());
                SwitchGrid switchGrid = switchGridBuilder.build();
                worldSettingsSection.addChild(switchGrid.layout());
                topRow.addChild(CommonLayouts.labeledElement(helper.getFont(), gameModeSection, GAME_MODE_LABEL));
                topRow.addChild(worldSettingsSection);
                this.layout.addChild(topRow, 0, 0, linearLayout.newCellSettings().alignHorizontallyLeft());

                /* Difficulty */
                GameTabButton peacefulButton = difficultySection.addChild(
                        GameTabButton.builder(
                                Component.translatable("options.difficulty.peaceful"),
                                _ -> uiState.setDifficulty(Difficulty.PEACEFUL),
                                Util.identifier("textures/gui/sprites/widget/difficulty/peaceful.png"),
                                Util.identifier("textures/gui/sprites/widget/difficulty/peaceful_highlighted.png"),
                                Util.identifier("textures/gui/sprites/widget/difficulty/peaceful_locked.png"),
                                DIFFICULTY_BUTTON_TEXTURE_WIDTH, DIFFICULTY_BUTTON_TEXTURE_HEIGHT
                        ).build()
                );
                peacefulButton.setSize(DIFFICULTY_BUTTON_WIDTH, DIFFICULTY_BUTTON_HEIGHT);
                peacefulButton.setTooltip(Tooltip.create(Difficulty.PEACEFUL.getInfo()));
                peacefulButton.setSelected(() -> uiState.getDifficulty() == Difficulty.PEACEFUL);

                GameTabButton easyButton = difficultySection.addChild(
                        GameTabButton.builder(
                                Component.translatable("options.difficulty.easy"),
                                _ -> uiState.setDifficulty(Difficulty.EASY),
                                Util.identifier("textures/gui/sprites/widget/difficulty/easy.png"),
                                Util.identifier("textures/gui/sprites/widget/difficulty/easy_highlighted.png"),
                                Util.identifier("textures/gui/sprites/widget/difficulty/easy_locked.png"),
                                DIFFICULTY_BUTTON_TEXTURE_WIDTH, DIFFICULTY_BUTTON_TEXTURE_HEIGHT
                        ).build()
                );
                easyButton.setSize(DIFFICULTY_BUTTON_WIDTH, DIFFICULTY_BUTTON_HEIGHT);
                easyButton.setTooltip(Tooltip.create(Difficulty.EASY.getInfo()));
                easyButton.setSelected(() -> uiState.getDifficulty() == Difficulty.EASY);

                GameTabButton normalButton = difficultySection.addChild(
                        GameTabButton.builder(
                                Component.translatable("options.difficulty.normal"),
                                _ -> uiState.setDifficulty(Difficulty.NORMAL),
                                Util.identifier("textures/gui/sprites/widget/difficulty/normal.png"),
                                Util.identifier("textures/gui/sprites/widget/difficulty/normal_highlighted.png"),
                                Util.identifier("textures/gui/sprites/widget/difficulty/normal_locked.png"),
                                DIFFICULTY_BUTTON_TEXTURE_WIDTH, DIFFICULTY_BUTTON_TEXTURE_HEIGHT
                        ).build()
                );
                normalButton.setSize(DIFFICULTY_BUTTON_WIDTH, DIFFICULTY_BUTTON_HEIGHT);
                normalButton.setTooltip(Tooltip.create(Difficulty.NORMAL.getInfo()));
                normalButton.setSelected(() -> uiState.getDifficulty() == Difficulty.NORMAL);

                GameTabButton hardButton = difficultySection.addChild(
                        GameTabButton.builder(
                                Component.translatable("options.difficulty.hard"),
                                _ -> uiState.setDifficulty(Difficulty.HARD),
                                Util.identifier("textures/gui/sprites/widget/difficulty/hard.png"),
                                Util.identifier("textures/gui/sprites/widget/difficulty/hard_highlighted.png"),
                                Util.identifier("textures/gui/sprites/widget/difficulty/hard_highlighted.png"),
                                DIFFICULTY_BUTTON_TEXTURE_WIDTH, DIFFICULTY_BUTTON_TEXTURE_HEIGHT
                        ).build()
                );
                hardButton.setSize(DIFFICULTY_BUTTON_WIDTH, DIFFICULTY_BUTTON_HEIGHT);
                hardButton.setTooltip(Tooltip.create(Difficulty.HARD.getInfo()));
                hardButton.setSelected(() -> uiState.getDifficulty() == Difficulty.HARD);

                uiState.addListener(worldCreationUiState -> {
                    this.nameEdit.setTooltip(Tooltip.create(Component.translatable("selectWorld.targetFolder",
                            Component.literal(worldCreationUiState.getTargetFolder()).withStyle(ChatFormatting.ITALIC))));
                    survivalButton.setLocked(uiState.isHardcore());
                    creativeButton.setActive(!uiState.isHardcore());
                    peacefulButton.setActive(!uiState.isHardcore());
                    easyButton.setActive(!uiState.isHardcore());
                    normalButton.setActive(!uiState.isHardcore());
                    hardButton.setLocked(uiState.isHardcore());

                    List<AbstractWidget> worldButtons = new ArrayList<>();
                    switchGrid.layout().visitWidgets(worldButtons::add);
                    int i = 0;
                    for (AbstractWidget widget : worldButtons) {
                        if (widget instanceof CycleButton<?> button) {
                            if (i == 1) {
                                ((CycleButton<Boolean>) button).setValue(uiState.isAllowCommands());
                                break;
                            }
                            i++;
                        }
                    }
                });
                LayoutElement bottomRow = CommonLayouts.labeledElement(helper.getFont(), difficultySection, DIFFICULTY_LABEL);
                this.layout.addChild(bottomRow, 1, 0, linearLayout.newCellSettings().alignHorizontallyLeft().paddingHorizontal(SPACING));
            }
        }

        /**
         * Prevents the original widgets from drawing
         */
        @Redirect(
                method = "<init>",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;Lnet/minecraft/client/gui/layouts/LayoutSettings;)Lnet/minecraft/client/gui/layouts/LayoutElement;"))
        private LayoutElement overrideVanilla(GridLayout.RowHelper instance, LayoutElement widget, LayoutSettings layoutSettings) {
            if (canChangeUi) {
                return widget;
            }
            return instance.addChild(widget, layoutSettings);
        }

        @Redirect(method = "<init>",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;"),
                require = 0)
        private LayoutElement overrideVanilla(GridLayout.RowHelper instance, LayoutElement widget) {
            if (canChangeUi) {
                return widget;
            }
            return instance.addChild(widget);
        }
    }
}
