package net.meander.subtlyd.client.gui.screens;

import com.mojang.datafixers.util.Pair;
import net.meander.subtlyd.client.gui.components.RatioSliderButton;
import net.meander.subtlyd.data.worldgen.WorldGeneratorSD;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.client.Minecraft;
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

public class CustomTerrainSettingsScreen extends Screen {
    private static final int LONG_SLIDER_WIDTH = 310;
    private static final int SLIDER_WIDTH = 150;
    private static final int SLIDER_HEIGHT = 20;
    private final Screen lastScreen;
    private final Minecraft minecraft = Minecraft.getInstance();
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 33, 33);
    private SliderList list;
    private AbstractSliderButton continentSlider;
    private AbstractSliderButton biomeSlider;
    private AbstractSliderButton erosionSlider;
    private AbstractSliderButton oceanSlider;
    public static final double MIN_VALUE = 0.1;
    public static final double MAX_VALUE = 10.0;
    public final double initialMaster;
    public final double initialContinent;
    public final double initialBiome;
    public final double initialErosion;
    public final double initialOceanDepth;

    public CustomTerrainSettingsScreen(Screen lastScreen) {
        this.lastScreen = lastScreen;
        initialMaster = CustomTerrainSettings.masterScale;
        initialContinent = CustomTerrainSettings.continentScale;
        initialBiome = CustomTerrainSettings.biomeScale;
        initialErosion = CustomTerrainSettings.erosionScale;
        initialOceanDepth = CustomTerrainSettings.oceanDepthScale;

        super(Component.translatable("createWorld.custom.title"));
    }

    @Override
    protected void init() {
        layout.addTitleHeader(title, font);

        list = layout.addToContents(new SliderList());

        list.addSingle(new RatioSliderButton(0, 0, LONG_SLIDER_WIDTH, SLIDER_HEIGHT, Component.translatable("createWorld.custom.master_scale"), MIN_VALUE, MAX_VALUE, CustomTerrainSettings.masterScale,
                (newValue) -> {
                    CustomTerrainSettings.applyMasterScale(newValue);
                    updateSliders();
                }
        ));

        continentSlider = new RatioSliderButton(0, 0, SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.custom.continent_scale"), MIN_VALUE, MAX_VALUE,
                CustomTerrainSettings.continentScale, v -> CustomTerrainSettings.continentScale = v
        );

        biomeSlider = new RatioSliderButton(0, 0, SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.custom.biome_scale"), MIN_VALUE, MAX_VALUE,
                CustomTerrainSettings.biomeScale, v -> CustomTerrainSettings.biomeScale = v
        );

        erosionSlider = new RatioSliderButton(0, 0, SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.custom.erosion_scale"), MIN_VALUE, MAX_VALUE,
                CustomTerrainSettings.erosionScale, v -> CustomTerrainSettings.erosionScale = v
        );

        oceanSlider = new RatioSliderButton(0, 0, SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.custom.ocean_depth_scale"), MIN_VALUE, MAX_VALUE,
                CustomTerrainSettings.oceanDepthScale, v -> CustomTerrainSettings.oceanDepthScale = v
        );

        list.addDouble(continentSlider, biomeSlider);
        list.addDouble(erosionSlider, oceanSlider);
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
            button.setRatioValue(CustomTerrainSettings.continentScale);
        }

        if (biomeSlider instanceof RatioSliderButton button) {
            button.setRatioValue(CustomTerrainSettings.biomeScale);
        }

        if (erosionSlider instanceof RatioSliderButton button) {
            button.setRatioValue(CustomTerrainSettings.erosionScale);
        }

        if (oceanSlider instanceof RatioSliderButton button) {
            button.setRatioValue(CustomTerrainSettings.oceanDepthScale);
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
                        (_) -> UtilSD.LOGGER.error("Minecraft aborted datapack reload! Malformed JSON syntax.")
                );
            }
        } else {
            minecraft.setScreenAndShow(lastScreen);
        }
    }

    private static WorldDataConfiguration getWorldDataConfiguration(WorldDataConfiguration currentConfig) {
        String packId = "file/custom_terrain";
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
        CustomTerrainSettings.masterScale = initialMaster;
        CustomTerrainSettings.continentScale = initialContinent;
        CustomTerrainSettings.biomeScale = initialBiome;
        CustomTerrainSettings.erosionScale = initialErosion;
        CustomTerrainSettings.oceanDepthScale = initialOceanDepth;

        minecraft.gui.setScreen(lastScreen);
    }

    @Override
    protected void extractMenuBackground(final GuiGraphicsExtractor graphics) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, CreateWorldScreen.TAB_HEADER_BACKGROUND, 0, 0, 0.0F, 0.0F, width, layout.getHeaderHeight(), 16, 16);
        extractMenuBackground(graphics, 0, layout.getHeaderHeight(), width, height);
    }

    private class SliderList extends ContainerObjectSelectionList<SliderList.Entry> {
        public SliderList() {
            super(CustomTerrainSettingsScreen.this.minecraft,
                    CustomTerrainSettingsScreen.this.width,
                    CustomTerrainSettingsScreen.this.height - layout.getHeaderHeight() - layout.getFooterHeight(),
                    layout.getHeaderHeight(),
                    24
            );
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
                int centerX = CustomTerrainSettingsScreen.this.width / 2;

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
