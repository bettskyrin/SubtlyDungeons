package com.kr1s1s.subtlyd.mixin.client.gui.screens.worldselection;

import com.kr1s1s.subtlyd.client.gui.components.GameTabButton;
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

    @Mixin(targets = "net.minecraft.client.gui.screens.worldselection.CreateWorldScreen$GameTab")
    public static class GameTabMixin extends GridLayoutTab {
        @Shadow
        @Final
        @Mutable
        private EditBox nameEdit;
        private static final Component TITLE = Component.translatable("createWorld.tab.game.title");
        private static final Component ALLOW_COMMANDS = Component.translatable("selectWorld.allowCommands");
        private static final Component NAME_LABEL = Component.translatable("selectWorld.enterName");

        public GameTabMixin(EditBox nameEdit) {
            super(TITLE);
            this.nameEdit = nameEdit;
        }

        @Inject(method = "<init>", at = @At("RETURN"))
        private void init(CreateWorldScreen helper, CallbackInfo ci) {
            LinearLayout linearLayout2 = new LinearLayout(0, 0, LinearLayout.Orientation.HORIZONTAL).spacing(4);
            linearLayout2.defaultCellSetting();
            LinearLayout linearLayout = new LinearLayout(0, 0, LinearLayout.Orientation.VERTICAL).spacing(4);
            linearLayout.defaultCellSetting().alignVerticallyMiddle();

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
            linearLayout2.addChild(
                    CommonLayouts.labeledElement(helper.getFont(), this.nameEdit, NAME_LABEL),
                    linearLayout2.newCellSettings().alignHorizontallyCenter()
            );

            GameTabButton survivalButton = linearLayout2.addChild(
                    GameTabButton.builder(
                            Component.translatable("selectWorld.gameMode.survival"),
                            _ -> helper.getUiState().setGameMode(WorldCreationUiState.SelectedGameMode.SURVIVAL),
                            com.kr1s1s.subtlyd.util.Util.identifier("textures/gui/sprites/widget/survival.png"),
                            200, 140
                    ).build()
            );
            survivalButton.setSize(100, 70);
            survivalButton.setTooltip(Tooltip.create(WorldCreationUiState.SelectedGameMode.SURVIVAL.getInfo()));

            GameTabButton creativeButton = linearLayout2.addChild(
                    GameTabButton.builder(
                            Component.translatable("selectWorld.gameMode.creative"),
                            _ -> helper.getUiState().setGameMode(WorldCreationUiState.SelectedGameMode.CREATIVE),
                            com.kr1s1s.subtlyd.util.Util.identifier("textures/gui/sprites/widget/creative.png"),
                            200, 140
                    ).build()
            );
            creativeButton.setSize(100, 70);
            creativeButton.setTooltip(Tooltip.create(WorldCreationUiState.SelectedGameMode.CREATIVE.getInfo()));

            this.layout.addChild(linearLayout2, 0, 0, linearLayout.newCellSettings().alignHorizontallyCenter());
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
