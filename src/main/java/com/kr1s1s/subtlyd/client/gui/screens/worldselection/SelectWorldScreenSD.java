package com.kr1s1s.subtlyd.client.gui.screens.worldselection;

import com.mojang.logging.LogUtils;
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
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FileUtil;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.storage.LevelSummary;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class SelectWorldScreenSD extends Screen {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final WorldOptions TEST_OPTIONS = new WorldOptions("test1".hashCode(), true, false);
    protected final Screen lastScreen;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 8 + 9 + 8 + 20 + 4, 30);
    @Nullable
    private Button deleteButton;
    @Nullable
    private Button selectButton;
    @Nullable
    private Button renameButton;
    @Nullable
    private Button copyButton;
    @Nullable
    protected EditBox searchBox;
    @Nullable
    private WorldSelectionListSD list;

    public SelectWorldScreenSD(Screen screen) {
        super(Component.translatable("selectWorld.title"));
        this.lastScreen = screen;
    }

    @Override
    protected void init() {
        int rowSpacing = 4;
        int bigButtonWidth = 64;
        int buttonWidth = 48;

        LinearLayout linearLayout = this.layout.addToHeader(LinearLayout.vertical().spacing(rowSpacing));
        linearLayout.defaultCellSetting().alignHorizontallyCenter();
        linearLayout.addChild(new StringWidget(this.title, this.font));
        LinearLayout linearLayout2 = linearLayout.addChild(LinearLayout.horizontal().spacing(rowSpacing));
        if (SharedConstants.DEBUG_WORLD_RECREATE) {
            linearLayout2.addChild(this.createDebugWorldRecreateButton());
        }

        this.searchBox = linearLayout2.addChild(
                new EditBox(this.font, this.width, 22, (int) (this.width / 2.5), 20, this.searchBox, Component.translatable("selectWorld.search"))
        );
        this.searchBox.setResponder(string -> {
            if (this.list != null) {
                this.list.updateFilter(string);
            }
        });
        this.searchBox.setHint(Component.translatable("gui.selectWorld.search").setStyle(EditBox.SEARCH_HINT_STYLE));

        Consumer<WorldSelectionListSD.WorldListEntry> consumer = WorldSelectionListSD.WorldListEntry::joinWorld;
        this.list = this.layout
                .addToContents(
                        new WorldSelectionListSD.Builder(this.minecraft, this)
                                .width(this.width)
                                .height(this.layout.getContentHeight())
                                .filter(this.searchBox.getValue())
                                .oldList(this.list)
                                .onEntrySelect(this::updateButtonStatus)
                                .onEntryInteract(consumer)
                                .build()
                );
        this.selectButton = linearLayout2.addChild(
                Button.builder(
                        LevelSummary.PLAY_WORLD, button -> this.list.getSelectedOpt().ifPresent(consumer)
                        )
                        .width(buttonWidth)
                        .build());
        this.renameButton = linearLayout2.addChild(
                Button.builder(
                        Component.translatable("selectWorld.edit"), button -> this.list.getSelectedOpt().ifPresent(WorldSelectionListSD.WorldListEntry::editWorld)
                        )
                        .width(buttonWidth)
                        .build()
        );
        this.copyButton = linearLayout2.addChild(
                Button.builder(
                        Component.translatable("selectWorld.recreate"), button -> this.list.getSelectedOpt().ifPresent(WorldSelectionListSD.WorldListEntry::recreateWorld)
                        )
                        .width(bigButtonWidth)
                        .build()
        );

        linearLayout2.addChild(new SpacerElement(this.width - (searchBox.getWidth() + bigButtonWidth + (buttonWidth * 3) + (rowSpacing * 7)), 0));

        this.deleteButton = linearLayout2.addChild(
                Button.builder(
                        Component.translatable("selectWorld.delete"), button -> this.list.getSelectedOpt().ifPresent(WorldSelectionListSD.WorldListEntry::deleteWorld)
                        )
                        .width(buttonWidth)
                        .build()
        );
        this.createFooterButtons(this.list);
        this.layout.visitWidgets(guiEventListener -> {
            AbstractWidget var10000 = this.addRenderableWidget(guiEventListener);
        });
        this.repositionElements();
        this.updateButtonStatus(null);
    }

    private void createFooterButtons(WorldSelectionListSD worldSelectionList) {
        int rowSpacing = 4;
        int halfButtonWidth = 100;
        int backButtonWidth = 60;

        GridLayout gridLayout = this.layout.addToFooter(new GridLayout().columnSpacing(8).rowSpacing(rowSpacing));
        gridLayout.defaultCellSetting().alignHorizontallyCenter();
        GridLayout.RowHelper rowHelper = gridLayout.createRowHelper(this.width);
        rowHelper.addChild(new SpacerElement(this.width / 2 - halfButtonWidth, 0));
        rowHelper.addChild(
                Button.builder(Component.translatable("selectWorld.create"), button -> CreateWorldScreen.openFresh(this.minecraft, worldSelectionList::returnToScreen))
                        .build()
        );

        rowHelper.addChild(new SpacerElement(this.width / 2 - (halfButtonWidth + (backButtonWidth / 2) + (2 * rowSpacing)), 0));

        rowHelper.addChild(
                Button.builder(
                        CommonComponents.GUI_BACK, button -> this.minecraft.setScreen(this.lastScreen))
                        .width(backButtonWidth)
                        .build());
    }

    private Button createDebugWorldRecreateButton() {
        return Button.builder(
                        Component.literal("DEBUG recreate"),
                        button -> {
                            try {
                                String string = "DEBUG world";
                                if (this.list != null && !this.list.children().isEmpty()) {
                                    WorldSelectionListSD.Entry entry = this.list.children().getFirst();
                                    if (entry instanceof WorldSelectionListSD.WorldListEntry worldListEntry && worldListEntry.getLevelName().equals(string)) {
                                        worldListEntry.doDeleteWorld();
                                    }
                                }

                                LevelSettings levelSettings = new LevelSettings(
                                        string,
                                        GameType.SPECTATOR,
                                        false,
                                        Difficulty.NORMAL,
                                        true,
                                        new GameRules(WorldDataConfiguration.DEFAULT.enabledFeatures()),
                                        WorldDataConfiguration.DEFAULT
                                );
                                String string2 = FileUtil.findAvailableName(this.minecraft.getLevelSource().getBaseDir(), string, "");
                                this.minecraft.createWorldOpenFlows().createFreshLevel(string2, levelSettings, TEST_OPTIONS, WorldPresets::createNormalWorldDimensions, this);
                            } catch (IOException var5) {
                                LOGGER.error("Failed to recreate the debug world", var5);
                            }
                        }
                )
                .width(72)
                .build();
    }

    @Override
    protected void repositionElements() {
        if (this.list != null) {
            this.list.updateSize(this.width, this.layout);
        }

        this.layout.arrangeElements();
    }

    @Override
    protected void setInitialFocus() {
        if (this.searchBox != null) {
            this.setInitialFocus(this.searchBox);
        }
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.lastScreen);
    }

    public void updateButtonStatus(@Nullable LevelSummary levelSummary) {
        if (this.selectButton != null && this.renameButton != null && this.copyButton != null && this.deleteButton != null) {
            if (levelSummary == null) {
                this.selectButton.setMessage(LevelSummary.PLAY_WORLD);
                this.selectButton.active = false;
                this.renameButton.active = false;
                this.copyButton.active = false;
                this.deleteButton.active = false;
            } else {
                this.selectButton.setMessage(levelSummary.primaryActionMessage());
                this.selectButton.active = levelSummary.primaryActionActive();
                this.renameButton.active = levelSummary.canEdit();
                this.copyButton.active = levelSummary.canRecreate();
                this.deleteButton.active = levelSummary.canDelete();
            }
        }
    }

    @Override
    public void removed() {
        if (this.list != null) {
            this.list.children().forEach(WorldSelectionListSD.Entry::close);
        }
    }
}