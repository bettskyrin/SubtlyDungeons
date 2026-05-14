package net.meander.subtlyd.mixin.client.gui.screens;

import net.meander.subtlyd.client.OptionInstanceSD;
import net.meander.subtlyd.client.renderer.GuiPlayerRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    @Shadow private boolean fading;
    private final int BUTTON_HEIGHT = 20;
    private final int SPRITE_XPOS = 4;
    private static final boolean canChangeUi = OptionInstanceSD.EXPERIMENTAL_GUI.get();

    protected TitleScreenMixin(Component component) {
        super(component);
    }

    /**
     * Corrects vertical spacing when using the experimental GUI.
     */
    @ModifyVariable(method = "init", at = @At(value = "STORE", ordinal = 2), name = "topPos")
    private int modifySpacing(int topPos) {
        if (canChangeUi) {
            return topPos - 12;
        }
        return topPos;
    }

    /**
     * Moves the language button to the bottom left corner of the screen.
     * @param args The original arguments that handled the position of the button.
     */
    @ModifyArgs(method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/SpriteIconButton;setPosition(II)V",
                    ordinal = 0))
    private void setLanguagePos(Args args) {
        if (canChangeUi) {
            args.set(0, SPRITE_XPOS);
            args.set(1, height - (BUTTON_HEIGHT + 4));
        }
    }

    /**
     * Moves the accessibility button to the bottom left corner of the screen, to the right of the language button.
     * @param args The original arguments that handled the position of the button.
     */
    @ModifyArgs(method = "init",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/SpriteIconButton;setPosition(II)V",
                    ordinal = 1))
    private void setAccessibilityPos(Args args) {
        if (canChangeUi) {
            args.set(0, SPRITE_XPOS + (BUTTON_HEIGHT + 4));
            args.set(1, height - (BUTTON_HEIGHT + 4));
        }
    }

    /**
     * Moves the friends button to the bottom left.
     * @param args The original arguments that handled the position of the button.
     */
    @ModifyArgs(method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/FriendsButton;setPosition(II)V"))
    private void setFriendsPos(Args args) {
        if (canChangeUi) {
            args.set(0, SPRITE_XPOS + (BUTTON_HEIGHT + 4) * 2);
            args.set(1, height - (BUTTON_HEIGHT + 4));
        }
    }

    /**
     * Prevents the update version from being rendered at the bottom of the screen. The update version may still be found via the Debug menu.
     */
    @Redirect(method = "extractRenderState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"))
    private void cancelVersion(GuiGraphicsExtractor graphics, Font font, String str, int x, int y, int color) {
        if (!canChangeUi) {
            graphics.text(font, str, x, y, color);
        }
    }

    /**
     * Renders the player in the bottom right corner of the screen after the fade animation is complete, as player shaders do not support transparency.
     */
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void renderPlayer(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        if (canChangeUi) {
            int AVATAR_SCALE = 40;

            if (!this.fading) {
                GuiPlayerRenderer.renderPlayer(graphics, this.width / 2 + 170, this.height / 4 + 132, AVATAR_SCALE, mouseX, mouseY);
            }
        }
    }
}
