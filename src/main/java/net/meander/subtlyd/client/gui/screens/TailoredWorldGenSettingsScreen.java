package net.meander.subtlyd.client.gui.screens;

import com.mojang.datafixers.util.Pair;
import net.meander.subtlyd.client.gui.components.RatioSliderButton;
import net.meander.subtlyd.data.worldgen.WorldGeneratorSD;
import net.meander.subtlyd.util.Util;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.WorldDataConfiguration;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TailoredWorldGenSettingsScreen extends Screen {
    private final double initialMaster;
    private final double initialContinent;
    private final double initialBiome;
    private final double initialErosion;
    private final Screen lastScreen;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 50, 50);
    private SliderList list;
    private AbstractSliderButton continentSlider;
    private AbstractSliderButton biomeSlider;
    private AbstractSliderButton erosionSlider;


    public TailoredWorldGenSettingsScreen(Screen parent) {
        lastScreen = parent;
        initialMaster = TailoredWorldGenSettings.masterScale;
        initialContinent = TailoredWorldGenSettings.continentScale;
        initialBiome = TailoredWorldGenSettings.biomeScale;
        initialErosion = TailoredWorldGenSettings.erosionScale;

        super(Component.translatable("createWorld.tailored.title"));
    }

    @Override
    protected void init() {
        final int LONG_SLIDER_WIDTH = 310;
        final int SLIDER_WIDTH = 150;
        final int SLIDER_HEIGHT = 20;
        final double MIN_VALUE = 0.1;
        final double MAX_VALUE = 10.0;

        layout.addTitleHeader(title, font);

        list = layout.addToContents(new SliderList());

        list.addSingle(new RatioSliderButton(0, 0, LONG_SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.tailored.master_scale"), MIN_VALUE, MAX_VALUE,
                TailoredWorldGenSettings.masterScale, (newValue) -> {
            TailoredWorldGenSettings.applyMasterScale(newValue);
            updateSliders();
        }));

        continentSlider = new RatioSliderButton(0, 0, SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.tailored.continent_scale"), MIN_VALUE, MAX_VALUE,
                TailoredWorldGenSettings.continentScale, v -> TailoredWorldGenSettings.continentScale = v);

        biomeSlider = new RatioSliderButton(0, 0, SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.tailored.biome_scale"), MIN_VALUE, MAX_VALUE,
                TailoredWorldGenSettings.biomeScale, v -> TailoredWorldGenSettings.biomeScale = v);

        erosionSlider = new RatioSliderButton(0, 0, SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.tailored.erosion_scale"), MIN_VALUE, MAX_VALUE,
                TailoredWorldGenSettings.erosionScale, v -> TailoredWorldGenSettings.erosionScale = v);

        list.addDouble(continentSlider, biomeSlider);
        list.addDouble(erosionSlider, null);
        createFooterButtons();
        layout.visitWidgets(this::addRenderableWidget);
        repositionElements();
    }

    private void createFooterButtons() {
        final int BUTTON_WIDTH = 150;
        LinearLayout footer = layout.addToFooter(LinearLayout.vertical().spacing(4));
        LinearLayout footerButtons = footer.addChild(LinearLayout.horizontal().spacing(8));

        footer.defaultCellSetting().alignHorizontallyCenter();

        footerButtons.addChild(Button.builder(CommonComponents.GUI_DONE, _ -> onDone()).width(BUTTON_WIDTH).build());
        footerButtons.addChild(Button.builder(CommonComponents.GUI_CANCEL, _ -> onCancel()).width(BUTTON_WIDTH).build());
    }

    @Override
    protected void repositionElements() {
        if (list != null) {
            list.updateSize(width, layout);
        }
        layout.arrangeElements();
    }

    private void updateSliders() {
        if (continentSlider instanceof RatioSliderButton button) {
            button.setRatioValue(TailoredWorldGenSettings.continentScale);
        }

        if (biomeSlider instanceof RatioSliderButton button) {
            button.setRatioValue(TailoredWorldGenSettings.biomeScale);
        }

        if (erosionSlider instanceof RatioSliderButton button) {
            button.setRatioValue(TailoredWorldGenSettings.erosionScale);
        }
    }

    private void onDone() {
        if (lastScreen instanceof CreateWorldScreen createScreen) {
            Path tempDataPackDir = createScreen.getOrCreateTempDataPackDir();
            WorldDataConfiguration config = createScreen.getUiState().getSettings().dataConfiguration();
            Pair<Path, PackRepository> settings = createScreen.getDataPackSelectionSettings(config);
            HolderLookup.Provider registries = createScreen.getUiState().getSettings().worldgenLoadContext();

            if (settings != null) {
                PackRepository tempRepo = settings.getSecond();

                WorldGeneratorSD.customizeWorldGeneration(tempDataPackDir, registries);
                tempRepo.reload();
                createScreen.applyNewPackConfig(
                        tempRepo,
                        getWorldDataConfiguration(config),
                        (_) -> Util.LOGGER.error("Minecraft aborted datapack reload! Malformed JSON syntax.")
                );
            }
        } else {
            minecraft.setScreenAndShow(lastScreen);
        }
    }

    private static WorldDataConfiguration getWorldDataConfiguration(WorldDataConfiguration currentConfig) {
        String packId = "file/tailored_worldgen";
        List<String> enabledPacks = new ArrayList<>(currentConfig.dataPacks().getEnabled());

        if (!enabledPacks.contains(packId)) {
            enabledPacks.add(packId);
        }

        return new WorldDataConfiguration(
                new DataPackConfig(enabledPacks, currentConfig.dataPacks().getDisabled()),
                currentConfig.enabledFeatures()
        );
    }

    private void onCancel() {
        TailoredWorldGenSettings.masterScale = initialMaster;
        TailoredWorldGenSettings.continentScale = initialContinent;
        TailoredWorldGenSettings.biomeScale = initialBiome;
        TailoredWorldGenSettings.erosionScale = initialErosion;

        minecraft.gui.setScreen(lastScreen);
    }

    @Override
    protected void extractMenuBackground(final GuiGraphicsExtractor graphics) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, CreateWorldScreen.TAB_HEADER_BACKGROUND, 0, 0, 0.0F, 0.0F, width, layout.getHeaderHeight(), 16, 16);
        extractMenuBackground(graphics, 0, layout.getHeaderHeight(), width, height);
    }

    private class SliderList extends ContainerObjectSelectionList<SliderList.Entry> {
        public SliderList() {
            super(TailoredWorldGenSettingsScreen.this.minecraft,
                    TailoredWorldGenSettingsScreen.this.width,
                    TailoredWorldGenSettingsScreen.this.height - layout.getHeaderHeight() - layout.getFooterHeight(),
                    layout.getHeaderHeight(),
                    24);
        }

        public void addSingle(AbstractWidget widget) {
            addEntry(new Entry(widget, null));
        }

        public void addDouble(AbstractWidget leftWidget, AbstractWidget rightWidget) {
            addEntry(new Entry(leftWidget, rightWidget));
        }

        public class Entry extends ContainerObjectSelectionList.Entry<SliderList.Entry> {
            private final AbstractWidget leftWidget;
            private final AbstractWidget rightWidget;
            private final List<AbstractWidget> activeWidgets;

            public Entry(AbstractWidget left, AbstractWidget right) {
                leftWidget = left;
                rightWidget = right;
                activeWidgets = new ArrayList<>();

                if (left != null) {
                    activeWidgets.add(left);
                }

                if (right != null) {
                    activeWidgets.add(right);
                }
            }

            @Override
            public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
                int centerX = TailoredWorldGenSettingsScreen.this.width / 2;

                if (leftWidget != null) {
                    leftWidget.setY(getContentY());

                    if (rightWidget == null && leftWidget.getWidth() > 150) {
                        leftWidget.setX(centerX - (leftWidget.getWidth() / 2));
                    } else {
                        leftWidget.setX(centerX - 155);
                    }

                    leftWidget.extractRenderState(graphics, mouseX, mouseY, a);
                }

                if (rightWidget != null) {
                    rightWidget.setY(getContentY());
                    rightWidget.setX(centerX + 5);
                    rightWidget.extractRenderState(graphics, mouseX, mouseY, a);
                }
            }

            @Override
            public List<? extends GuiEventListener> children() {
                return activeWidgets;
            }

            @Override
            public List<? extends NarratableEntry> narratables() {
                return activeWidgets;
            }
        }
    }
}
