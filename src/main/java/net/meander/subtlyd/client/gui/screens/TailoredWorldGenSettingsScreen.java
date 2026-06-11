package net.meander.subtlyd.client.gui.screens;

import net.meander.subtlyd.client.gui.components.ScaleSlider;
import net.meander.subtlyd.data.WorldGeneratorSD;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.WorldDataConfiguration;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TailoredWorldGenSettingsScreen extends Screen {
    private final double defaultMaster;
    private final double defaultContinent;
    private final double defaultClimate;
    private final double defaultOcean;
    private final Screen parentScreen;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private AbstractSliderButton continentSlider;
    private AbstractSliderButton climateSlider;
    private AbstractSliderButton oceanSlider;


    public TailoredWorldGenSettingsScreen(Screen parent) {
        parentScreen = parent;
        defaultMaster = TailoredWorldGenSettings.masterScale;
        defaultContinent = TailoredWorldGenSettings.continentScale;
        defaultClimate = TailoredWorldGenSettings.climateScale;
        defaultOcean = TailoredWorldGenSettings.oceanDepth;

        super(Component.translatable("createWorld.customize.tailored.title"));
    }

    @Override
    protected void init() {
        final int ROW_SPACING = 12;
        final int SPACING = 8;
        final int LONG_SLIDER_WIDTH = 310;
        final int SLIDER_WIDTH = 150;
        final int SLIDER_HEIGHT = 20;
        final double MIN_VALUE = 0.1;
        final double MAX_VALUE = 10.0;

        layout.addToHeader(new StringWidget(title, font));

        LinearLayout contentLayout = layout.addToContents(LinearLayout.vertical().spacing(ROW_SPACING));

        contentLayout.addChild(new ScaleSlider(0, 0, LONG_SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.customize.tailored.master_scale").getString(), MIN_VALUE, MAX_VALUE,
                TailoredWorldGenSettings.masterScale, (newValue) -> {
            TailoredWorldGenSettings.applyMasterScale(newValue);
            updateSliders();
        }));

        LinearLayout row1 = LinearLayout.horizontal().spacing(SPACING);

        continentSlider = row1.addChild(new ScaleSlider(0, 0, SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.customize.tailored.continent_scale").getString(), MIN_VALUE, MAX_VALUE,
                TailoredWorldGenSettings.continentScale, v -> TailoredWorldGenSettings.continentScale = v)
        );

        climateSlider = row1.addChild(new ScaleSlider(0, 0, SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.customize.tailored.climate_smoothness").getString(), MIN_VALUE, MAX_VALUE,
                TailoredWorldGenSettings.climateScale, v -> TailoredWorldGenSettings.climateScale = v)
        );
        contentLayout.addChild(row1);

        LinearLayout row2 = LinearLayout.horizontal().spacing(SPACING);

        oceanSlider = row2.addChild(new ScaleSlider(0, 0, SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.customize.tailored.ocean_depth").getString(), MIN_VALUE, MAX_VALUE,
                TailoredWorldGenSettings.oceanDepth, v -> TailoredWorldGenSettings.oceanDepth = v)
        );
        contentLayout.addChild(row2);

        LinearLayout footerLayout = layout.addToFooter(LinearLayout.horizontal().spacing(SPACING));

        footerLayout.addChild(Button.builder(CommonComponents.GUI_DONE, (_) -> onDone()).build());
        footerLayout.addChild(Button.builder(CommonComponents.GUI_CANCEL, (_) -> onCancel()).build());

        layout.visitWidgets(this::addRenderableWidget);
        repositionElements();
    }

    @Override
    protected void repositionElements() {
        layout.arrangeElements();
    }

    private void updateSliders() {
        if (continentSlider instanceof ScaleSlider s) s.setValueFromConfig(TailoredWorldGenSettings.continentScale);
        if (climateSlider instanceof ScaleSlider s) s.setValueFromConfig(TailoredWorldGenSettings.climateScale);
        if (oceanSlider instanceof ScaleSlider s) s.setValueFromConfig(TailoredWorldGenSettings.oceanDepth);
    }

    private void onDone() {
        TailoredWorldGenSettings.shouldAlterSettings = true;

        if (parentScreen instanceof CreateWorldScreen createScreen) {
            Path tempPackDir = createScreen.getOrCreateTempDataPackDir();
            Path myDatapackDir;

            if (tempPackDir != null) {
                myDatapackDir = tempPackDir.resolve("subtlyd_worldgen");

                WorldGeneratorSD.modifyWorldGeneration(myDatapackDir);

                PackRepository packRepository = createScreen.tempDataPackRepository;
                if (packRepository != null) {
                    packRepository.reload();

                    WorldCreationUiState uiState = createScreen.getUiState();
                    WorldDataConfiguration newConfig = getWorldDataConfiguration(uiState, packRepository);

                    uiState.tryUpdateDataConfiguration(newConfig);
                }
            }
        }
        minecraft.setScreenAndShow(parentScreen);
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
    private void onCancel() { // TODO
        TailoredWorldGenSettings.masterScale = defaultMaster;
        TailoredWorldGenSettings.continentScale = defaultContinent;
        TailoredWorldGenSettings.climateScale = defaultClimate;
        TailoredWorldGenSettings.oceanDepth = defaultOcean;

        TailoredWorldGenSettings.shouldAlterSettings = true;

        minecraft.setScreenAndShow(parentScreen);
    }
}
