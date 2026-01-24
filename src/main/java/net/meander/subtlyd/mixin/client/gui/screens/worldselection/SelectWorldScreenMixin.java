package net.meander.subtlyd.mixin.client.gui.screens.worldselection;

import net.meander.subtlyd.util.Util;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
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
    @Shadow @Nullable private Button deleteButton;
    @Shadow @Nullable private Button selectButton;
    @Shadow @Nullable private Button renameButton;
    @Shadow @Nullable private Button copyButton;
    @Shadow protected EditBox searchBox;
    @Shadow private WorldSelectionList list;
    private final int ROW_SPACING = 4;

    private SelectWorldScreenMixin(HeaderAndFooterLayout layout, Component title, Screen lastScreen) {
        super(title);
        this.layout = layout;
        this.lastScreen = lastScreen;
    }

    @Shadow private void createFooterButtons(final Consumer<WorldSelectionList.WorldListEntry> joinWorld, final WorldSelectionList list) {}
    @Shadow private Button createDebugWorldRecreateButton() {
        throw new AssertionError();
    }

    /**
     * Changes the location of the world option buttons.
     */
    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void init(CallbackInfo ci) {
        ci.cancel();
        int bigButtonWidth = 64;
        int buttonWidth = 48;

        this.layout.setFooterHeight(35);
        LinearLayout linearLayout = this.layout.addToHeader(LinearLayout.vertical().spacing(ROW_SPACING));
        linearLayout.defaultCellSetting().alignHorizontallyCenter();
        linearLayout.addChild(new StringWidget(this.title, this.font));
        LinearLayout linearLayout2 = linearLayout.addChild(LinearLayout.horizontal().spacing(ROW_SPACING));
        if (SharedConstants.DEBUG_WORLD_RECREATE) {
            linearLayout2.addChild(this.createDebugWorldRecreateButton());
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

        linearLayout2.addChild(new SpacerElement(this.width - (searchBox.getWidth() + bigButtonWidth + (buttonWidth * 3) + (ROW_SPACING * 7)), 0));

        this.deleteButton = linearLayout2.addChild(
                Button.builder(Component.translatable("selectWorld.delete"), _ -> this.list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::deleteWorld))
                        .width(buttonWidth)
                        .build());
        Consumer<WorldSelectionList.WorldListEntry> joinWorld = WorldSelectionList.WorldListEntry::joinWorld;

        this.createFooterButtons(joinWorld, this.list);
        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
        selectWorldScreen.updateButtonStatus(null);
    }


    /**
     * Alter footer button design
     */
    @Inject(method = "createFooterButtons", at = @At("HEAD"), cancellable = true)
    private void createFooterButtons(Consumer<WorldSelectionList.WorldListEntry> joinWorld, WorldSelectionList list, CallbackInfo ci) {
        ci.cancel();
        int BUTTON_MIDDLE_X = 100;

        GridLayout gridLayout = this.layout.addToFooter(new GridLayout().columnSpacing(8).rowSpacing(ROW_SPACING));
        gridLayout.defaultCellSetting().alignHorizontallyCenter();
        GridLayout.RowHelper rowHelper = gridLayout.createRowHelper(this.width);
        rowHelper.addChild(new SpacerElement(this.width / 2 - (BUTTON_MIDDLE_X - (ROW_SPACING * 3)), 0));
        rowHelper.addChild(
                Button.builder(Component.translatable("selectWorld.create"), _ -> CreateWorldScreen.openFresh(this.minecraft, list::returnToScreen))
                        .build()
        );

        rowHelper.addChild(new SpacerElement(this.width / 2 - (BUTTON_MIDDLE_X + (Util.Globals.BACK_BUTTON_WIDTH / 2) + (7 * ROW_SPACING)), 0));

        rowHelper.addChild(
                Button.builder(
                                CommonComponents.GUI_BACK, _ -> this.minecraft.setScreen(this.lastScreen))
                        .width(Util.Globals.BACK_BUTTON_WIDTH)
                        .build());
    }
}
