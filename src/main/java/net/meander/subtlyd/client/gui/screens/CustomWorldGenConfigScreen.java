package net.meander.subtlyd.client.gui.screens;

import net.meander.subtlyd.client.gui.components.ScaleSlider;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CustomWorldGenConfigScreen extends Screen {
    private final Screen parent;
    private AbstractSliderButton continentSlider;
    private AbstractSliderButton climateSlider;
    private AbstractSliderButton oceanSlider;

    public CustomWorldGenConfigScreen(Screen parent) {
        super(Component.translatable("createWorld.customize.custom.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        final int CENTER = width / 2;
        final int LEFT_ALIGNMENT = CENTER + 5;
        final int RIGHT_ALIGNMENT = CENTER - 155;
        final int START_Y = 40;
        final int SPACING = 24;
        final int LONG_SLIDER_WIDTH = 310;
        final int SLIDER_WIDTH = 150;
        final int SLIDER_HEIGHT = 20;
        final double MIN_VALUE = 0.1;
        final double MAX_VALUE = 10.0;

        addRenderableWidget(new ScaleSlider(
                RIGHT_ALIGNMENT, START_Y, LONG_SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.customize.custom.master_scale").getString(), MIN_VALUE, MAX_VALUE,
                TailoredWorldGenConfig.masterScale, (newValue) -> {
                    TailoredWorldGenConfig.applyMasterScale(newValue);
                    updateSliders();
                }
        ));

        continentSlider = addRenderableWidget(new ScaleSlider(RIGHT_ALIGNMENT, START_Y + SPACING, SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.customize.custom.continent_scale").getString(), MIN_VALUE, MAX_VALUE,
                TailoredWorldGenConfig.continentScale, v -> TailoredWorldGenConfig.continentScale = v)
        );

        climateSlider = addRenderableWidget(new ScaleSlider(LEFT_ALIGNMENT, START_Y + SPACING, SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.customize.custom.climate_smoothness").getString(), MIN_VALUE, MAX_VALUE,
                TailoredWorldGenConfig.climateScale, v -> TailoredWorldGenConfig.climateScale = v)
        );

        oceanSlider = addRenderableWidget(new ScaleSlider(RIGHT_ALIGNMENT, START_Y + SPACING * 2, SLIDER_WIDTH, SLIDER_HEIGHT,
                Component.translatable("createWorld.customize.custom.ocean_depth").getString(), MIN_VALUE, MAX_VALUE,
                TailoredWorldGenConfig.oceanDepth, v -> TailoredWorldGenConfig.oceanDepth = v)
        );

        addRenderableWidget(Button.builder(Component.translatable("gui.done"), (_) -> minecraft.setScreenAndShow(parent))
                .bounds(CENTER - 100, height - 40, 200, SLIDER_HEIGHT)
                .build()
        );
    }

    private void updateSliders() {
        if (continentSlider instanceof ScaleSlider s) s.setValueFromConfig(TailoredWorldGenConfig.continentScale);
        if (climateSlider instanceof ScaleSlider s) s.setValueFromConfig(TailoredWorldGenConfig.climateScale);
        if (oceanSlider instanceof ScaleSlider s) s.setValueFromConfig(TailoredWorldGenConfig.oceanDepth);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.centeredText(font, title, width / 2, 15, 16777215);
    }
}
