package net.meander.subtlyd.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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

public abstract class GameTabButton extends AbstractButton {
    protected static final GameTabButton.CreateNarration DEFAULT_NARRATION = Supplier::get;
    protected final GameTabButton.OnPress onPress;
    protected final GameTabButton.CreateNarration createNarration;
    protected final Identifier texture;
    protected final Identifier hoverTexture;
    protected final Identifier lockedTexture;
    protected final int textureWidth;
    protected final int textureHeight;
    protected BooleanSupplier isSelected = () -> false;
    protected boolean isLocked = false;

    public static GameTabButton.Builder builder(Component component, GameTabButton.OnPress onPress, Identifier texture, Identifier hoverTexture, Identifier disabledTexture, int textureWidth, int textureHeight) {
        return new GameTabButton.Builder(component, onPress, texture, hoverTexture, disabledTexture, textureWidth, textureHeight);
    }

    public GameTabButton(int x, int y, int width, int height, Component component, GameTabButton.OnPress onPress, GameTabButton.CreateNarration createNarration, Identifier texture, Identifier hoverTexture, Identifier lockedTexture, int textureWidth, int textureHeight) {
        super(x, y, width, height, component);
        this.onPress = onPress;
        this.createNarration = createNarration;
        this.texture = texture;
        this.hoverTexture = hoverTexture;
        this.lockedTexture = lockedTexture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public Identifier getTexture() {
        if (!isActive() || isLocked()) {
            return lockedTexture;
        } else if (isHoveredOrFocused() || isSelected()) {
            return hoverTexture;
        }
        return texture;
    }

    public boolean isSelected() {
        return isSelected.getAsBoolean();
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setSelected(BooleanSupplier bl) {
        isSelected = bl;
    }

    public void setLocked(Boolean bl) {
        isLocked = bl;
    }

    public void setActive(Boolean bl) {
        active = bl;
    }

    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }

    @Override
    public void onPress(@NonNull InputWithModifiers inputWithModifiers) {
        onPress.onPress(this);
    }

    public static class Builder {
        private final Component message;
        private final GameTabButton.OnPress onPress;
        private int x;
        private int y;
        private int width;
        private int height;
        private final GameTabButton.CreateNarration createNarration = GameTabButton.DEFAULT_NARRATION;
        private final Identifier texture;
        private final Identifier hoverTexture;
        private final Identifier lockedTexture;
        private int textureWidth = 0;
        private int textureHeight = 0;

        public Builder(Component message, GameTabButton.OnPress onPress, Identifier texture, Identifier hoverTexture, Identifier lockedTexture, int width, int height) {
            this.message = message;
            this.onPress = onPress;
            this.texture = texture;
            this.hoverTexture = hoverTexture;
            this.lockedTexture = lockedTexture;
            this.width = width;
            this.height = height;
        }

        public GameTabButton.Builder pos(int x, int y) {
            this.x = x;
            this.y = y;

            return this;
        }

        public GameTabButton.Builder size(int width, int height) {
            this.width = width;
            this.height = height;

            return this;
        }

        public GameTabButton build() {
            textureWidth = (textureWidth == 0) ? width : textureWidth;
            textureHeight = (textureHeight == 0) ? height : textureHeight;

            return new Plain(x, y, width, height, message, onPress, createNarration, texture, hoverTexture, lockedTexture, textureWidth, textureHeight);
        }
    }

    @SuppressWarnings("unused")
    public interface CreateNarration {
        MutableComponent createNarrationMessage(Supplier<MutableComponent> supplier);
    }

    public interface OnPress {
        void onPress(GameTabButton button);
    }

    public static class Plain extends GameTabButton {
        private final Font font = Minecraft.getInstance().font;

        protected Plain(int i, int j, int k, int l, Component component, GameTabButton.OnPress onPress, GameTabButton.CreateNarration createNarration, Identifier texture, Identifier hoverTexture, Identifier disabledTexture, int textureWidth, int textureHeight) {
            super(i, j, k, l, component, onPress, createNarration, texture, hoverTexture, disabledTexture, textureWidth, textureHeight);
        }

        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
            boolean wasHovered = isHovered;
            int textColor = active ? 0xFFFFFFFF : 0xA0A0A0A0;
            Identifier renderedImage = getTexture();

            if (isSelected()) { // Makes the vanilla button texture portion render as highlighted when selected
                isHovered = true;
            }

            extractDefaultSprite(graphics);

            isHovered = wasHovered;

            if (renderedImage != null) {
                Matrix3x2fStack pose = graphics.pose();
                pose.pushMatrix();

                pose.translate(getX(), getY());

                float scaleX = (float) getWidth() / textureWidth;
                float scaleY = (float) getHeight() / textureHeight;
                pose.scale(scaleX, scaleY);

                graphics.blit(
                        RenderPipelines.GUI_TEXTURED,
                        renderedImage,
                        0, 0, 0.0F, 0.0F,
                        textureWidth, textureHeight,
                        textureWidth, textureHeight,
                        -1
                );
                pose.popMatrix();
            }

            graphics.centeredText(font,
                    getMessage(),
                    getX() + (width / 2),
                    getY() + getHeight() - 13,
                    textColor
            );
        }
    }
}
