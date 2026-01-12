package com.kr1s1s.subtlyd.mixin.client.gui.screens.worldselection;

import com.kr1s1s.subtlyd.client.gui.components.GameTabButton;
import com.kr1s1s.subtlyd.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.layouts.*;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.SwitchGrid;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    @Shadow
    @Final
    private WorldCreationUiState uiState;

    /**
     * Changes the Game Tab Layout
     */
    @Mixin(targets = "net.minecraft.client.gui.screens.worldselection.CreateWorldScreen$GameTab")
    public static class GameTabMixin extends GridLayoutTab {
        private static final Component TITLE = Component.translatable("createWorld.tab.game.title");
        private static final Component NAME_LABEL = Component.translatable("selectWorld.enterName");
        private static final Component GAME_MODE_LABEL = Component.translatable("selectWorld.gameMode");
        private static final Component ALLOW_COMMANDS = Component.translatable("selectWorld.allowCommands");
        private static final Component ALLOW_COMMANDS_INFO = Component.translatable("selectWorld.allowCommands.info");
        private static final Component HARDCORE = Component.translatable("selectWorld.gameMode.hardcore");
        private static final Component HARDCORE_INFO = Component.translatable("selectWorld.gameMode.hardcore.info");
        @Shadow @Final @Mutable private EditBox nameEdit;

        public GameTabMixin(EditBox nameEdit) {
            super(TITLE);
            this.nameEdit = nameEdit;
        }

        @Inject(method = "<init>", at = @At("RETURN"))
        private void init(CreateWorldScreen helper, CallbackInfo ci) {
            int SPACING = 4;
            int GAME_MODE_BUTTON_WIDTH = 112;
            int GAME_MODE_BUTTON_HEIGHT = 82;
            int GAME_MODE_BUTTON_TEXTURE_WIDTH = 192;
            int GAME_MODE_BUTTON_TEXTURE_HEIGHT = 141;
            int RIGHT_WIDGET_WIDGET = (int) (helper.width / 2.5);
            LinearLayout linearLayout = new LinearLayout(0, 0, LinearLayout.Orientation.VERTICAL).spacing(SPACING);
            LinearLayout topRow = new LinearLayout(0, 0, LinearLayout.Orientation.HORIZONTAL).spacing(SPACING);
            LinearLayout leftColumn = new LinearLayout(0, 0, LinearLayout.Orientation.HORIZONTAL).spacing(SPACING);
            LinearLayout rightColumn = new LinearLayout(0, 0, LinearLayout.Orientation.VERTICAL).spacing(SPACING);
            SwitchGrid.Builder switchGridBuilder = SwitchGrid.builder(RIGHT_WIDGET_WIDGET);
            WorldCreationUiState uiState = helper.getUiState();

            linearLayout.defaultCellSetting().alignVerticallyMiddle();
            topRow.defaultCellSetting().paddingHorizontal(4);
            rightColumn.defaultCellSetting().alignHorizontallyCenter();

            GameTabButton survivalButton = leftColumn.addChild(
                    GameTabButton.builder(
                            Component.translatable("selectWorld.gameMode.survival"),
                            _ -> uiState.setGameMode(WorldCreationUiState.SelectedGameMode.SURVIVAL),
                            Util.identifier("textures/gui/sprites/widget/survival.png"),
                            Util.identifier("textures/gui/sprites/widget/survival_highlighted.png"),
                            Util.identifier("textures/gui/sprites/widget/survival_locked.png"),
                            GAME_MODE_BUTTON_TEXTURE_WIDTH, GAME_MODE_BUTTON_TEXTURE_HEIGHT
                    ).build()
            );
            survivalButton.setSize(GAME_MODE_BUTTON_WIDTH, GAME_MODE_BUTTON_HEIGHT);
            survivalButton.setTooltip(Tooltip.create(WorldCreationUiState.SelectedGameMode.SURVIVAL.getInfo()));
            survivalButton.setIsSelected(() -> uiState.getGameMode() == WorldCreationUiState.SelectedGameMode.SURVIVAL);
            survivalButton.active = uiState.getGameMode() != WorldCreationUiState.SelectedGameMode.HARDCORE;

            GameTabButton creativeButton = leftColumn.addChild(
                    GameTabButton.builder(
                            Component.translatable("selectWorld.gameMode.creative"),
                            _ -> {
                                uiState.setGameMode(WorldCreationUiState.SelectedGameMode.CREATIVE);
                                uiState.setAllowCommands(true);
                            },
                            Util.identifier("textures/gui/sprites/widget/creative.png"),
                            Util.identifier("textures/gui/sprites/widget/creative_highlighted.png"),
                            Util.identifier("textures/gui/sprites/widget/creative_disabled.png"),
                            GAME_MODE_BUTTON_TEXTURE_WIDTH, GAME_MODE_BUTTON_TEXTURE_HEIGHT
                    ).build()
            );
            creativeButton.setSize(GAME_MODE_BUTTON_WIDTH, GAME_MODE_BUTTON_HEIGHT);
            creativeButton.setTooltip(Tooltip.create(WorldCreationUiState.SelectedGameMode.CREATIVE.getInfo()));
            creativeButton.setIsSelected(() -> uiState.getGameMode() == WorldCreationUiState.SelectedGameMode.CREATIVE);

            this.nameEdit = new EditBox(helper.getFont(), RIGHT_WIDGET_WIDGET, 20, Component.translatable("selectWorld.enterName"));
            this.nameEdit.setValue(uiState.getName());
            this.nameEdit.setResponder(uiState::setName);
            uiState.addListener(worldCreationUiState -> {
                this.nameEdit.setTooltip(Tooltip.create(Component.translatable("selectWorld.targetFolder",
                                Component.literal(worldCreationUiState.getTargetFolder()).withStyle(ChatFormatting.ITALIC))));
                survivalButton.setIsLocked(uiState.isHardcore());
                creativeButton.setIsActive(!uiState.isHardcore());
            });
            helper.setInitialFocus(this.nameEdit);

            switchGridBuilder.addSwitch(HARDCORE, uiState::isHardcore, (isHardcore) -> {
                uiState.setGameMode(isHardcore ? WorldCreationUiState.SelectedGameMode.HARDCORE : WorldCreationUiState.SelectedGameMode.SURVIVAL);
                if (isHardcore) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.FLINTANDSTEEL_USE, 1.0F));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.FIRECHARGE_USE, 0.8F));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.FIRE_AMBIENT, 0.5F));
                }
            }).withInfo(HARDCORE_INFO);
            switchGridBuilder.addSwitch(ALLOW_COMMANDS, uiState::isAllowCommands, uiState::setAllowCommands).withInfo(ALLOW_COMMANDS_INFO);
            rightColumn.addChild(
                    CommonLayouts.labeledElement(helper.getFont(), this.nameEdit, NAME_LABEL),
                    topRow.newCellSettings().alignHorizontallyCenter());
            rightColumn.addChild(switchGridBuilder.build().layout());
            topRow.addChild(CommonLayouts.labeledElement(helper.getFont(), leftColumn, GAME_MODE_LABEL));
            topRow.addChild(rightColumn);
            this.layout.addChild(topRow, 0, 0, linearLayout.newCellSettings().alignHorizontallyCenter());
        }

        /**
         * Prevents the original widgets from drawing
         */
        @Redirect(
                method = "<init>",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;Lnet/minecraft/client/gui/layouts/LayoutSettings;)Lnet/minecraft/client/gui/layouts/LayoutElement;"))
        private LayoutElement overrideVanilla(GridLayout.RowHelper instance, LayoutElement widget, LayoutSettings settings) {
            return widget;
        }

        @Redirect(method = "<init>",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;"),
                require = 0)
        private LayoutElement overrideVanilla(GridLayout.RowHelper instance, LayoutElement widget) {
            return widget;
        }
    }
}
