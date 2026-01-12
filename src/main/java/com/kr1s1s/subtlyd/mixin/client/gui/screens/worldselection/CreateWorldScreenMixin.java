package com.kr1s1s.subtlyd.mixin.client.gui.screens.worldselection;

import com.kr1s1s.subtlyd.client.gui.components.GameTabButton;
import com.kr1s1s.subtlyd.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.layouts.*;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.network.chat.Component;
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
    /**
     * Changes the Game Tab Layout
     */
    @Mixin(targets = "net.minecraft.client.gui.screens.worldselection.CreateWorldScreen$GameTab")
    public static class GameTabMixin extends GridLayoutTab {
        private static final Component TITLE = Component.translatable("createWorld.tab.game.title");
        private static final Component ALLOW_COMMANDS = Component.translatable("selectWorld.allowCommands");
        private static final Component NAME_LABEL = Component.translatable("selectWorld.enterName");
        private static final Component GAME_MODE_LABEL = Component.translatable("selectWorld.gameMode");
        @Shadow @Final @Mutable private EditBox nameEdit;

        public GameTabMixin(EditBox nameEdit) {
            super(TITLE);
            this.nameEdit = nameEdit;
        }

        @Inject(method = "<init>", at = @At("RETURN"))
        private void init(CreateWorldScreen helper, CallbackInfo ci) {
            int SPACING = 4;
            int GAME_MODE_BUTTON_WIDTH = 114;
            int GAME_MODE_BUTTON_HEIGHT = 80;
            int GAME_MODE_BUTTON_TEXTURE_WIDTH = 200;
            int GAME_MODE_BUTTON_TEXTURE_HEIGHT = 140;
            LinearLayout linearLayout = new LinearLayout(0, 0, LinearLayout.Orientation.VERTICAL).spacing(SPACING);
            LinearLayout topRow = new LinearLayout(0, 0, LinearLayout.Orientation.HORIZONTAL).spacing(SPACING);
            LinearLayout leftColumn = new LinearLayout(0, 0, LinearLayout.Orientation.HORIZONTAL).spacing(SPACING);

            linearLayout.defaultCellSetting().alignVerticallyMiddle();
            topRow.defaultCellSetting().paddingHorizontal(4);
            leftColumn.defaultCellSetting().alignHorizontallyLeft();

            GameTabButton survivalButton = leftColumn.addChild(
                    GameTabButton.builder(
                            Component.translatable("selectWorld.gameMode.survival"),
                            _ -> helper.getUiState().setGameMode(WorldCreationUiState.SelectedGameMode.SURVIVAL),
                            Util.identifier("textures/gui/sprites/widget/survival.png"),
                            Util.identifier("textures/gui/sprites/widget/survival_highlighted.png"),
                            GAME_MODE_BUTTON_TEXTURE_WIDTH, GAME_MODE_BUTTON_TEXTURE_HEIGHT
                    ).build()
            );
            survivalButton.setSize(GAME_MODE_BUTTON_WIDTH, GAME_MODE_BUTTON_HEIGHT);
            survivalButton.setTooltip(Tooltip.create(WorldCreationUiState.SelectedGameMode.SURVIVAL.getInfo()));

            GameTabButton creativeButton = leftColumn.addChild(
                    GameTabButton.builder(
                            Component.translatable("selectWorld.gameMode.creative"),
                            _ -> helper.getUiState().setGameMode(WorldCreationUiState.SelectedGameMode.CREATIVE),
                            Util.identifier("textures/gui/sprites/widget/creative.png"),
                            Util.identifier("textures/gui/sprites/widget/creative_highlighted.png"),
                            GAME_MODE_BUTTON_TEXTURE_WIDTH, GAME_MODE_BUTTON_TEXTURE_HEIGHT
                    ).build()
            );
            creativeButton.setSize(GAME_MODE_BUTTON_WIDTH, GAME_MODE_BUTTON_HEIGHT);
            creativeButton.setTooltip(Tooltip.create(WorldCreationUiState.SelectedGameMode.CREATIVE.getInfo()));

            topRow.addChild(CommonLayouts.labeledElement(helper.getFont(), leftColumn, GAME_MODE_LABEL));
            this.layout.addChild(topRow, 0, 0, linearLayout.newCellSettings().alignHorizontallyCenter());

            this.nameEdit = new EditBox(helper.getFont(), (int) (helper.width / 2.5), 20, Component.translatable("selectWorld.enterName"));
            this.nameEdit.setValue(helper.getUiState().getName());
            this.nameEdit.setResponder(helper.getUiState()::setName);
            helper.getUiState()
                    .addListener(
                            worldCreationUiState -> this.nameEdit
                                    .setTooltip(
                                            Tooltip.create(
                                                    Component.translatable("selectWorld.targetFolder", Component.literal(worldCreationUiState.getTargetFolder()).withStyle(ChatFormatting.ITALIC))
                                            )
                                    )
                    );
            helper.setInitialFocus(this.nameEdit);
            topRow.addChild(
                    CommonLayouts.labeledElement(helper.getFont(), this.nameEdit, NAME_LABEL),
                    topRow.newCellSettings().alignHorizontallyCenter()
            );
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
