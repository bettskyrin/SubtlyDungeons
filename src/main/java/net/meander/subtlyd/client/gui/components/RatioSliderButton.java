package net.meander.subtlyd.client.gui.components;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import java.util.function.Consumer;

public class RatioSliderButton extends AbstractSliderButton {
    private final String prefix;
    private final double min;
    private final double max;
    private final Consumer<Double> onValueChanged;

    public RatioSliderButton(int x, int y, int width, int height, String prefix, double min, double max, double currentValue, Consumer<Double> onValueChanged) {
        super(x, y, width, height, Component.empty(), (currentValue - min) / (max - min));
        this.prefix = prefix;
        this.min = min;
        this.max = max;
        this.onValueChanged = onValueChanged;

        updateMessage();
    }

    @Override
    protected void updateMessage() {
        double realValue = min + (value * (max - min));
        long level = Math.round(realValue * 10.0);

        setMessage(Component.literal(String.format("%s: %d", prefix, level)));
    }

    @Override
    protected void applyValue() {
        double realValue = min + (value * (max - min));

        onValueChanged.accept(realValue);
    }

    public void setRatioValue(final double newValue) {
        value = (newValue - min) / (max - min);

        updateMessage();
    }
}