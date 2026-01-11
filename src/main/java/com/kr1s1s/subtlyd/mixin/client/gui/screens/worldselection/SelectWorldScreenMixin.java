package com.kr1s1s.subtlyd.mixin.client.gui.screens.worldselection;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.LevelSummary;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(SelectWorldScreen.class)
public abstract class SelectWorldScreenMixin extends Screen {
    private final SelectWorldScreen selectWorldScreen = (SelectWorldScreen) (Object) this;
    @Shadow @Final @Mutable protected final Screen lastScreen;
    @Shadow @Final @Mutable private final HeaderAndFooterLayout layout;
    @Shadow private @Nullable Button deleteButton;
    @Shadow private @Nullable Button selectButton;
    @Shadow private @Nullable Button renameButton;
    @Shadow private @Nullable Button copyButton;
    @Shadow protected EditBox searchBox;
    @Shadow private WorldSelectionList list;


    private SelectWorldScreenMixin(HeaderAndFooterLayout layout, Component title, Screen lastScreen) {
        super(title);
        this.layout = layout;
        this.lastScreen = lastScreen;
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void init(CallbackInfo ci) {
        ci.cancel();
        int rowSpacing = 4;
        int bigButtonWidth = 64;
        int buttonWidth = 48;

        LinearLayout linearLayout = this.layout.addToHeader(LinearLayout.vertical().spacing(rowSpacing));
        linearLayout.defaultCellSetting().alignHorizontallyCenter();
        linearLayout.addChild(new StringWidget(this.title, this.font));
        LinearLayout linearLayout2 = linearLayout.addChild(LinearLayout.horizontal().spacing(rowSpacing));
        if (SharedConstants.DEBUG_WORLD_RECREATE) {
            linearLayout2.addChild(selectWorldScreen.createDebugWorldRecreateButton());
        }

        this.searchBox = linearLayout2.addChild(
                new EditBox(this.font, this.width, 22, (int) (this.width / 2.5), 20, this.searchBox, Component.translatable("selectWorld.search")));
        this.searchBox.setResponder(string -> {
            if (this.list != null) {
                this.list.updateFilter(string);
            }
        });
        this.searchBox.setHint(Component.translatable("gui.selectWorld.search").setStyle(EditBox.SEARCH_HINT_STYLE));

        Consumer<WorldSelectionList.WorldListEntry> consumer = WorldSelectionList.WorldListEntry::joinWorld;
        this.list = this.layout.addToContents(new WorldSelectionList.Builder(this.minecraft, selectWorldScreen)
                                .width(this.width)
                                .height(this.layout.getContentHeight())
                                .filter(this.searchBox.getValue())
                                .oldList(this.list)
                                .onEntrySelect(selectWorldScreen::updateButtonStatus)
                                .onEntryInteract(consumer)
                                .build());
        this.selectButton = linearLayout2.addChild(
                Button.builder(LevelSummary.PLAY_WORLD, _ -> this.list.getSelectedOpt().ifPresent(consumer))
                        .width(buttonWidth)
                        .build());
        this.renameButton = linearLayout2.addChild(
                Button.builder(Component.translatable("selectWorld.edit"), _ -> this.list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::editWorld))
                        .width(buttonWidth)
                        .build());
        this.copyButton = linearLayout2.addChild(
                Button.builder(Component.translatable("selectWorld.recreate"), _ -> this.list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::recreateWorld))
                        .width(bigButtonWidth)
                        .build());

        linearLayout2.addChild(new SpacerElement(this.width - (searchBox.getWidth() + bigButtonWidth + (buttonWidth * 3) + (rowSpacing * 7)), 0));

        this.deleteButton = linearLayout2.addChild(
                Button.builder(Component.translatable("selectWorld.delete"), _ -> this.list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::deleteWorld))
                        .width(buttonWidth)
                        .build());
        Consumer<WorldSelectionList.WorldListEntry> joinWorld = WorldSelectionList.WorldListEntry::joinWorld;

        selectWorldScreen.createFooterButtons(joinWorld, this.list);
        this.layout.visitWidgets(guiEventListener -> {
            AbstractWidget var10000 = this.addRenderableWidget(guiEventListener); // TODO Remove?
        });
        this.repositionElements();
        selectWorldScreen.updateButtonStatus(null);
    }

    @Inject(method = "createFooterButtons", at = @At("HEAD"), cancellable = true)
    private void createFooterButtons(Consumer<WorldSelectionList.WorldListEntry> joinWorld, WorldSelectionList list, CallbackInfo ci) {
        ci.cancel();
        int rowSpacing = 4;
        int halfButtonWidth = 100;
        int backButtonWidth = 60;

        GridLayout gridLayout = this.layout.addToFooter(new GridLayout().columnSpacing(8).rowSpacing(rowSpacing));
        gridLayout.defaultCellSetting().alignHorizontallyCenter();
        GridLayout.RowHelper rowHelper = gridLayout.createRowHelper(this.width);
        rowHelper.addChild(new SpacerElement(this.width / 2 - (halfButtonWidth - (rowSpacing * 3)), 0));
        rowHelper.addChild(
                Button.builder(Component.translatable("selectWorld.create"), _ -> CreateWorldScreen.openFresh(this.minecraft, list::returnToScreen))
                        .build()
        );

        rowHelper.addChild(new SpacerElement(this.width / 2 - (halfButtonWidth + (backButtonWidth / 2) + (7 * rowSpacing)), 0));

        rowHelper.addChild(
                Button.builder(
                                CommonComponents.GUI_BACK, _ -> this.minecraft.setScreen(this.lastScreen))
                        .width(backButtonWidth)
                        .build());
    }
}
