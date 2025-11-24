package com.kr1s1s.subtlyd.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2fStack;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class GameTabButton extends AbstractButton {
    protected static final GameTabButton.CreateNarration DEFAULT_NARRATION = supplier -> (MutableComponent)supplier.get();
    protected final GameTabButton.OnPress onPress;
    protected final GameTabButton.CreateNarration createNarration;
    protected final Identifier resourceLocation;
    protected final int textureWidth;
    protected final int textureHeight;

    public static GameTabButton.Builder builder(Component component, GameTabButton.OnPress onPress, Identifier resourceLocation, int textureWidth, int textureHeight) {
        return new GameTabButton.Builder(component, onPress, resourceLocation, textureWidth, textureHeight);
    }

    public GameTabButton(int x, int y, int width, int height, Component component, GameTabButton.OnPress onPress, GameTabButton.CreateNarration createNarration, Identifier resourceLocation, int textureWidth, int textureHeight) {
        super(x, y, width, height, component);
        this.onPress = onPress;
        this.createNarration = createNarration;
        this.resourceLocation = resourceLocation;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }


    @Override
    public void onPress(InputWithModifiers inputWithModifiers) {

    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int i, int j, float f) {

    }

    @Environment(EnvType.CLIENT)
    public static class Builder {
        private final Component message;
        private final GameTabButton.OnPress onPress;
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width;
        private int height;
        private GameTabButton.CreateNarration createNarration = GameTabButton.DEFAULT_NARRATION;
        private Identifier resourceLocation;
        private int textureWidth = 0;
        private int textureHeight = 0;

        public Builder(Component component, GameTabButton.OnPress onPress, Identifier resourceLocation, int width, int height) {
            this.message = component;
            this.onPress = onPress;
            this.resourceLocation = resourceLocation;
            this.width = width;
            this.height = height;
        }

        public GameTabButton.Builder pos(int i, int j) {
            this.x = i;
            this.y = j;
            return this;
        }

        public GameTabButton.Builder width(int i) {
            this.width = i;
            return this;
        }

        public GameTabButton.Builder size(int i, int j) {
            this.width = i;
            this.height = j;
            return this;
        }

        public GameTabButton.Builder textureSize(int i, int j) {
            this.textureWidth = i;
            this.textureHeight = j;
            return this;
        }

        public GameTabButton.Builder bounds(int i, int j, int k, int l) {
            return this.pos(i, j).size(k, l);
        }

        public GameTabButton.Builder texture(Identifier resourceLocation) {
            this.resourceLocation = resourceLocation;
            return this;
        }

        public GameTabButton.Builder tooltip(Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public GameTabButton.Builder createNarration(GameTabButton.CreateNarration createNarration) {
            this.createNarration = createNarration;
            return this;
        }

        public GameTabButton build() {
            this.textureWidth = (this.textureWidth == 0) ? this.width : this.textureWidth;
            this.textureHeight = (this.textureHeight == 0) ? this.height : this.textureHeight;
            GameTabButton button = new GameTabButton.Plain(this.x, this.y, this.width, this.height, this.message, this.onPress, this.createNarration, this.resourceLocation, this.textureWidth, this.textureHeight);
            button.setTooltip(this.tooltip);
            return button;
        }
    }

    @Environment(EnvType.CLIENT)
    public interface CreateNarration {
        MutableComponent createNarrationMessage(Supplier<MutableComponent> supplier);
    }

    @Environment(EnvType.CLIENT)
    public interface OnPress {
        void onPress(GameTabButton button);
    }
    @Environment(EnvType.CLIENT)
    public static class Plain extends GameTabButton {
        protected Plain(int i, int j, int k, int l, Component component, GameTabButton.OnPress onPress, GameTabButton.CreateNarration createNarration, Identifier resourceLocation, int textureWidth, int textureHeight) {
            super(i, j, k, l, component, onPress, createNarration, resourceLocation, textureWidth, textureHeight);
        }

        @Override
        protected void renderContents(GuiGraphics guiGraphics, int i, int j, float f) {
            this.renderDefaultSprite(guiGraphics);

            if (this.resourceLocation != null) {
                Matrix3x2fStack pose = guiGraphics.pose();
                pose.pushMatrix();

                pose.translate(this.getX(), this.getY());

                float scaleX = (float) this.getWidth() / this.textureWidth;
                float scaleY = (float) this.getHeight() / this.textureHeight;
                pose.scale(scaleX, scaleY);

                guiGraphics.blit(
                        RenderPipelines.GUI_TEXTURED,
                        this.resourceLocation,
                        0, 0, 0.0F, 0.0F,
                        this.textureWidth, this.textureHeight,
                        this.textureWidth, this.textureHeight,
                        -1
                );

                pose.popMatrix();
            }

            int textColor = this.active ? 0xFFFFFF : 0xA0A0A0;

            guiGraphics.drawCenteredString(Minecraft.getInstance().font,
                    this.getMessage(),
                    this.getX() + this.width / 2,
                    this.getY() - (this.height),
                    textColor
            );

            this.renderDefaultLabel(guiGraphics.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE));
        }
    }


}
