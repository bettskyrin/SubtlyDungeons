package net.meander.subtlyd.mixin.client.gui.screens.worldselection;

import net.meander.subtlyd.client.OptionsSD;
import net.meander.subtlyd.util.UtilSD;
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
    @Shadow @Final @Mutable protected final Screen lastScreen;
    @Shadow @Final @Mutable private final HeaderAndFooterLayout layout;
    @Shadow @Nullable private Button deleteButton;
    @Shadow @Nullable private Button playWorldButton;
    @Shadow @Nullable private Button editButton;
    @Shadow @Nullable private Button recreateButton;
    @Shadow protected EditBox searchBox;
    @Shadow private WorldSelectionList list;
    private final int ROW_SPACING = 4;
    private static final boolean canChangeUi = OptionsSD.gui().get();

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
        if (canChangeUi) {
            final SelectWorldScreen selectWorldScreen = (SelectWorldScreen) (Object) this;
            final int LARGE_BUTTON_WIDTH = 64;
            final int BUTTON_WIDTH = 48;

            layout.setFooterHeight(35);
            LinearLayout linearLayout = layout.addToHeader(LinearLayout.vertical().spacing(ROW_SPACING));
            linearLayout.defaultCellSetting().alignHorizontallyCenter();
            linearLayout.addChild(new StringWidget(title, font));
            LinearLayout linearLayout2 = linearLayout.addChild(LinearLayout.horizontal().spacing(ROW_SPACING));
            if (SharedConstants.DEBUG_WORLD_RECREATE) {
                linearLayout2.addChild(createDebugWorldRecreateButton());
            }

            searchBox = linearLayout2.addChild(
                    new EditBox(font, width, 22, (int) (width / 2.5), 20, searchBox, Component.translatable("selectWorld.search")));
            searchBox.setResponder(string -> {
                if (list != null) {
                    list.updateFilter(string);
                }
            });
            searchBox.setHint(Component.translatable("gui.selectWorld.search").setStyle(EditBox.SEARCH_HINT_STYLE));

            Consumer<WorldSelectionList.WorldListEntry> consumer = WorldSelectionList.WorldListEntry::joinWorld;
            list = layout.addToContents(new WorldSelectionList.Builder(minecraft, selectWorldScreen)
                    .width(width)
                    .height(layout.getContentHeight())
                    .filter(searchBox.getValue())
                    .oldList(list)
                    .onEntrySelect(selectWorldScreen::updateButtonStatus)
                    .onEntryInteract(consumer)
                    .build());
            playWorldButton = linearLayout2.addChild(
                    Button.builder(LevelSummary.PLAY_WORLD, _ -> list.getSelectedOpt().ifPresent(consumer))
                            .width(BUTTON_WIDTH)
                            .build());
            editButton = linearLayout2.addChild(
                    Button.builder(Component.translatable("selectWorld.edit"), _ -> list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::editWorld))
                            .width(BUTTON_WIDTH)
                            .build());
            recreateButton = linearLayout2.addChild(
                    Button.builder(Component.translatable("selectWorld.recreate"), _ -> list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::recreateWorld))
                            .width(LARGE_BUTTON_WIDTH)
                            .build());

            linearLayout2.addChild(new SpacerElement(width - (searchBox.getWidth() + LARGE_BUTTON_WIDTH + (BUTTON_WIDTH * 3) + (ROW_SPACING * 7)), 0));

            deleteButton = linearLayout2.addChild(
                    Button.builder(Component.translatable("selectWorld.delete"), _ -> list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::deleteWorld))
                            .width(BUTTON_WIDTH)
                            .build());
            Consumer<WorldSelectionList.WorldListEntry> joinWorld = WorldSelectionList.WorldListEntry::joinWorld;

            createFooterButtons(joinWorld, list);
            layout.visitWidgets(this::addRenderableWidget);
            repositionElements();
            selectWorldScreen.updateButtonStatus(null);
            ci.cancel();
        }
    }

    /**
     * Modify footer button design
     */
    @Inject(method = "createFooterButtons", at = @At("HEAD"), cancellable = true)
    private void createFooterButtons(Consumer<WorldSelectionList.WorldListEntry> joinWorld, WorldSelectionList list, CallbackInfo ci) {
        if (canChangeUi) {
            int BUTTON_MIDDLE_X = 100;

            GridLayout gridLayout = layout.addToFooter(new GridLayout().columnSpacing(8).rowSpacing(ROW_SPACING));
            gridLayout.defaultCellSetting().alignHorizontallyCenter();
            GridLayout.RowHelper rowHelper = gridLayout.createRowHelper(width);

            rowHelper.addChild(new SpacerElement(width / 2 - (BUTTON_MIDDLE_X - (ROW_SPACING * 3)), 0));
            rowHelper.addChild(
                    Button.builder(Component.translatable("selectWorld.create"), _ -> CreateWorldScreen.openFresh(minecraft, list::returnToScreen))
                            .build()
            );

            rowHelper.addChild(new SpacerElement(width / 2 - (BUTTON_MIDDLE_X + (UtilSD.GUI_COMMON.BACK_BUTTON_WIDTH / 2) + (7 * ROW_SPACING)), 0));
            rowHelper.addChild(
                    Button.builder(CommonComponents.GUI_BACK, _ -> minecraft.setScreenAndShow(lastScreen))
                            .width(UtilSD.GUI_COMMON.BACK_BUTTON_WIDTH)
                            .build()
            );
            ci.cancel();
        }
    }
}
