package net.meander.subtlyd.client.gui.screens;

import net.meander.subtlyd.client.gui.components.ScaleSliderButton;
import net.meander.subtlyd.data.WorldGeneratorSD;
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
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.client.renderer.RenderPipelines;
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
    private final double initialClimate;
    private final double initialOcean;
    private final Screen lastScreen;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 50, 50);
    private SliderList list;
    private AbstractSliderButton continentSlider;
    private AbstractSliderButton climateSlider;
    private AbstractSliderButton oceanSlider;


    public TailoredWorldGenSettingsScreen(Screen parent) {
        lastScreen = parent;
        initialMaster = TailoredWorldGenSettings.masterScale;
        initialContinent = TailoredWorldGenSettings.continentScale;
        initialClimate = TailoredWorldGenSettings.climateScale;
        initialOcean = TailoredWorldGenSettings.oceanDepth;

        super(Component.translatable("createWorld.customize.tailored.title"));
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

        list.addSingle(new ScaleSliderButton(0, 0, LONG_SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.customize.tailored.master_scale").getString(), MIN_VALUE, MAX_VALUE,
                TailoredWorldGenSettings.masterScale, (newValue) -> {
            TailoredWorldGenSettings.applyMasterScale(newValue);
            updateSliders();
        }));

        continentSlider = new ScaleSliderButton(0, 0, SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.customize.tailored.continent_scale").getString(), MIN_VALUE, MAX_VALUE,
                TailoredWorldGenSettings.continentScale, v -> TailoredWorldGenSettings.continentScale = v);

        climateSlider = new ScaleSliderButton(0, 0, SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.customize.tailored.climate_smoothness").getString(), MIN_VALUE, MAX_VALUE,
                TailoredWorldGenSettings.climateScale, v -> TailoredWorldGenSettings.climateScale = v);

        oceanSlider = new ScaleSliderButton(0, 0, SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.customize.tailored.ocean_depth").getString(), MIN_VALUE, MAX_VALUE,
                TailoredWorldGenSettings.oceanDepth, v -> TailoredWorldGenSettings.oceanDepth = v);

        list.addDouble(continentSlider, climateSlider);
        list.addSingle(oceanSlider);
        createFooterButtons();
        layout.visitWidgets(this::addRenderableWidget);
        repositionElements();
    }

    private void createFooterButtons() {
        final int BUTTON_WIDTH = 150;
        LinearLayout footer = layout.addToFooter(LinearLayout.vertical().spacing(4));
        footer.defaultCellSetting().alignHorizontallyCenter();
        LinearLayout footerButtons = footer.addChild(LinearLayout.horizontal().spacing(8));

        footerButtons.addChild(Button.builder(CommonComponents.GUI_DONE, _ -> {
            onDone();
            minecraft.gui.setScreen(lastScreen);
        }).width(BUTTON_WIDTH).build());

        footerButtons.addChild(Button.builder(CommonComponents.GUI_CANCEL, _ -> {
            onCancel();
            minecraft.gui.setScreen(lastScreen);
        }).width(BUTTON_WIDTH).build());
    }

    @Override
    protected void repositionElements() {
        if (list != null) {
            list.updateSize(width, layout);
        }
        layout.arrangeElements();
    }

    private void updateSliders() {
        if (continentSlider instanceof ScaleSliderButton s) s.setValueFromConfig(TailoredWorldGenSettings.continentScale);
        if (climateSlider instanceof ScaleSliderButton s) s.setValueFromConfig(TailoredWorldGenSettings.climateScale);
        if (oceanSlider instanceof ScaleSliderButton s) s.setValueFromConfig(TailoredWorldGenSettings.oceanDepth);
    }

    private void onDone() {
        TailoredWorldGenSettings.shouldAlterSettings = true;

        if (lastScreen instanceof CreateWorldScreen createScreen) {
            Path tailoredPackDir;
            Path tempPackDir = createScreen.getOrCreateTempDataPackDir();

            if (tempPackDir != null) {
                PackRepository packRepository = createScreen.tempDataPackRepository;
                tailoredPackDir = tempPackDir.resolve("subtlyd_worldgen");

                WorldGeneratorSD.modifyWorldGeneration(tailoredPackDir);

                if (packRepository != null) {
                    packRepository.reload();

                    WorldCreationUiState uiState = createScreen.getUiState();
                    WorldDataConfiguration newConfig = getWorldDataConfiguration(uiState, packRepository);

                    uiState.tryUpdateDataConfiguration(newConfig);
                }
            }
        }
    }

    private static WorldDataConfiguration getWorldDataConfiguration(WorldCreationUiState uiState, PackRepository repository) {
        WorldDataConfiguration currentConfig = uiState.getSettings().dataConfiguration();
        List<String> enabledPacks = new ArrayList<>(currentConfig.dataPacks().getEnabled());

        String packId = repository.getAvailableIds().stream()
                .filter(id -> id.contains("subtlyd_worldgen"))
                .findFirst()
                .orElse("file/subtlyd_worldgen");

        if (!enabledPacks.contains(packId)) {
            enabledPacks.add(packId);
        }

        DataPackConfig newPackConfig = new DataPackConfig(enabledPacks, currentConfig.dataPacks().getDisabled());
        return new WorldDataConfiguration(newPackConfig, currentConfig.enabledFeatures());
    }
    private void onCancel() {
        TailoredWorldGenSettings.masterScale = initialMaster;
        TailoredWorldGenSettings.continentScale = initialContinent;
        TailoredWorldGenSettings.climateScale = initialClimate;
        TailoredWorldGenSettings.oceanDepth = initialOcean;
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
                int center = TailoredWorldGenSettingsScreen.this.width / 2;

                if (leftWidget != null) {
                    leftWidget.setY(getContentY());

                    if (rightWidget == null && leftWidget.getWidth() > 150) {
                        leftWidget.setX(center - (leftWidget.getWidth() / 2));
                    } else {
                        leftWidget.setX(center - 155);
                    }
                    leftWidget.extractRenderState(graphics, mouseX, mouseY, a);
                }

                if (rightWidget != null) {
                    rightWidget.setY(getContentY());
                    rightWidget.setX(center + 5);
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
