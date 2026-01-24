package com.meander.subtlyd.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.NonNull;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class GameTabButton extends AbstractButton {
    protected static final GameTabButton.CreateNarration DEFAULT_NARRATION = Supplier::get;
    protected final GameTabButton.OnPress onPress;
    protected final GameTabButton.CreateNarration createNarration;
    protected final Identifier textureLocation;
    protected final Identifier hoverTextureLocation;
    protected final Identifier lockedTextureLocation;
    protected final int textureWidth;
    protected final int textureHeight;
    protected BooleanSupplier isSelected = () -> false;
    protected boolean isLocked = false;

    public static GameTabButton.Builder builder(Component component, GameTabButton.OnPress onPress, Identifier texture, Identifier hoverTexture, Identifier disabledTexture, int textureWidth, int textureHeight) {
        return new GameTabButton.Builder(component, onPress, texture, hoverTexture, disabledTexture, textureWidth, textureHeight);
    }

    public GameTabButton(int x, int y, int width, int height, Component component, GameTabButton.OnPress onPress, GameTabButton.CreateNarration createNarration, Identifier texture, Identifier hoverTexture, Identifier lockedTextureLocation, int textureWidth, int textureHeight) {
        super(x, y, width, height, component);
        this.onPress = onPress;
        this.createNarration = createNarration;
        this.textureLocation = texture;
        this.hoverTextureLocation = hoverTexture;
        this.lockedTextureLocation = lockedTextureLocation;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public Identifier getTextureLocation() {
        if (!this.isActive() || this.isLocked()) {
            return this.lockedTextureLocation;
        } else if (this.isHoveredOrFocused() || isSelected()) {
            return this.hoverTextureLocation;
        }
        return this.textureLocation;
    }

    public boolean isSelected() {
        return this.isSelected.getAsBoolean();
    }

    public boolean isLocked() {
        return this.isLocked;
    }

    public void setSelected(BooleanSupplier bl) {
        this.isSelected = bl;
    }

    public void setLocked(Boolean bl) {
        this.isLocked = bl;
    }

    public void setActive(Boolean bl) {
        this.active = bl;
    }

    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {
        this.defaultButtonNarrationText(output);
    }

    @Override
    public void onPress(@NonNull InputWithModifiers inputWithModifiers) {
        this.onPress.onPress(this);
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {
        private final Component message;
        private final GameTabButton.OnPress onPress;
        private int x;
        private int y;
        private int width;
        private int height;
        private final GameTabButton.CreateNarration createNarration = GameTabButton.DEFAULT_NARRATION;
        private final Identifier textureLocation;
        private final Identifier hoverTextureLocation;
        private final Identifier lockedTextureLocation;
        private int textureWidth = 0;
        private int textureHeight = 0;

        public Builder(Component component, GameTabButton.OnPress onPress, Identifier texture, Identifier hoverTexture, Identifier lockedTextureLocation, int width, int height) {
            this.message = component;
            this.onPress = onPress;
            this.textureLocation = texture;
            this.hoverTextureLocation = hoverTexture;
            this.lockedTextureLocation = lockedTextureLocation;
            this.width = width;
            this.height = height;
        }

        public GameTabButton.Builder pos(int i, int j) {
            this.x = i;
            this.y = j;
            return this;
        }

        public GameTabButton.Builder size(int i, int j) {
            this.width = i;
            this.height = j;
            return this;
        }

        public GameTabButton.Builder bounds(int i, int j, int k, int l) {
            return this.pos(i, j).size(k, l);
        }


        public GameTabButton build() {
            this.textureWidth = (this.textureWidth == 0) ? this.width : this.textureWidth;
            this.textureHeight = (this.textureHeight == 0) ? this.height : this.textureHeight;
            return new Plain(this.x, this.y, this.width, this.height, this.message, this.onPress, this.createNarration, this.textureLocation, this.hoverTextureLocation, this.lockedTextureLocation, this.textureWidth, this.textureHeight);
        }
    }

    @Environment(EnvType.CLIENT)
    @SuppressWarnings("unused")
    public interface CreateNarration {
        MutableComponent createNarrationMessage(Supplier<MutableComponent> supplier);
    }

    @Environment(EnvType.CLIENT)
    public interface OnPress {
        void onPress(GameTabButton button);
    }

    @Environment(EnvType.CLIENT)
    public static class Plain extends GameTabButton {
        private final Font font = Minecraft.getInstance().font;

        protected Plain(int i, int j, int k, int l, Component component, GameTabButton.OnPress onPress, GameTabButton.CreateNarration createNarration, Identifier texture, Identifier hoverTexture, Identifier disabledTexture, int textureWidth, int textureHeight) {
            super(i, j, k, l, component, onPress, createNarration, texture, hoverTexture, disabledTexture, textureWidth, textureHeight);
        }

        @Override
        protected void renderContents(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float a) {
            boolean wasHovered = this.isHovered;
            int textColor = this.active ? 0xFFFFFFFF : 0xA0A0A0A0;
            Identifier renderedImage = getTextureLocation();

            if (this.isSelected()) { // Makes the vanilla button texture portion render as highlighted when selected
                this.isHovered = true;
            }

            this.renderDefaultSprite(graphics);
            this.isHovered = wasHovered;
            if (renderedImage != null) {
                Matrix3x2fStack pose = graphics.pose();
                pose.pushMatrix();

                pose.translate(this.getX(), this.getY());

                float scaleX = (float) this.getWidth() / this.textureWidth;
                float scaleY = (float) this.getHeight() / this.textureHeight;
                pose.scale(scaleX, scaleY);

                graphics.blit(
                        RenderPipelines.GUI_TEXTURED,
                        renderedImage,
                        0, 0, 0.0F, 0.0F,
                        this.textureWidth, this.textureHeight,
                        this.textureWidth, this.textureHeight,
                        -1
                );
                pose.popMatrix();
            }

            graphics.drawCenteredString(this.font,
                    this.getMessage(),
                    this.getX() + (this.width / 2),
                    this.getY() + this.getHeight() - 13,
                    textColor
            );
        }
    }
}
