package net.meander.subtlyd.mixin.client.gui.screens;

import net.meander.subtlyd.client.renderer.GuiPlayerRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.CommonButtons;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.options.LanguageSelectScreen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;
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

    protected TitleScreenMixin(Component component) {
        super(component);
    }

    /**
     * Prevents the language button from being created.
     * @return Null
     */
    @Nullable
    @Redirect(method = "init",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/TitleScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;",
                    ordinal = 0))
    private GuiEventListener cancelDeclaration(TitleScreen instance, GuiEventListener guiEventListener) {
        return null;
    }

    /**
     * Prevents the language button position from being set.
     */
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/SpriteIconButton;setPosition(II)V", ordinal = 0))
    private void cancelPosition(SpriteIconButton instance, int i, int j) {}

    /**
     * Moves the language button to the bottom left corner of the screen.
     */
    @Inject(method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/TitleScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;",
                    ordinal = 3))
    private void moveLang(CallbackInfo ci) {
        SpriteIconButton spriteIconButton = this.addRenderableWidget(CommonButtons.language(
                20,
                _ -> this.minecraft.setScreenAndShow(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager())),
                true)
        );
        spriteIconButton.setPosition(SPRITE_XPOS, height - (4 + BUTTON_HEIGHT));
    }

    /**
     * Sets the language button to be the constant BUTTON_HEIGHT value.
     * @return The size of the button.
     */
    @ModifyArg(method = "init", at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/components/CommonButtons;language(ILnet/minecraft/client/gui/components/Button$OnPress;Z)Lnet/minecraft/client/gui/components/SpriteIconButton;"),
                index = 0)
    private int languageButton(int i) {
        return BUTTON_HEIGHT;
    }

    /**
     * Sets the accessibility button to be the constant BUTTON_HEIGHT value.
     * @return The size of the button.
     */
    @ModifyArg(method = "init", at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/components/CommonButtons;accessibility(ILnet/minecraft/client/gui/components/Button$OnPress;Z)Lnet/minecraft/client/gui/components/SpriteIconButton;"),
                index = 0)
    private int accessibilityButton(int i) {
        return BUTTON_HEIGHT;
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
    private void setAccPos(Args args) {
        args.set(0, SPRITE_XPOS + BUTTON_HEIGHT + 4);
        args.set(1, height - (4 + BUTTON_HEIGHT));
    }

    /**
     * Prevents the update version from being rendered at the bottom of the screen. The update version may still be found via the Debug menu.
     */
    @Redirect(method = "extractRenderState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"))
    private void cancelVersion(GuiGraphicsExtractor instance, Font font, String str, int x, int y, int color) {}

    /**
     * Renders the player in the bottom right corner of the screen after the fade animation is complete, as player shaders do not support transparency.
     */
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void renderPlayer(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        int CHARACTER_SCALE = 40;
        if (!this.fading) {
            GuiPlayerRenderer.renderPlayer(graphics, this.width / 2 + 170, this.height / 4 + 132, CHARACTER_SCALE, mouseX, mouseY);
        }
    }
}
