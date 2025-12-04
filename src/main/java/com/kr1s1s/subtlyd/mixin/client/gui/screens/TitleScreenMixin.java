package com.kr1s1s.subtlyd.mixin.client.gui.screens;

import com.kr1s1s.subtlyd.client.gui.screens.worldselection.SelectWorldScreenSD;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.options.LanguageSelectScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    int BUTTON_HEIGHT = 20;
    int SPRITE_XPOS = 4;

    protected TitleScreenMixin(Component component) {
        super(component);
    }

    @Redirect(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/TitleScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;",
                    ordinal = 0
            )
    )
    private GuiEventListener cancelDeclaration(TitleScreen instance, GuiEventListener guiEventListener) {
        return null;
    }

    @Redirect(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/SpriteIconButton;setPosition(II)V",
                    ordinal = 0
            )
    )
    private void cancelPosition(SpriteIconButton instance, int i, int j) {}

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/TitleScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;",
                    ordinal = 3
            )
    )
    private void move(CallbackInfo ci) {
        SpriteIconButton spriteIconButton = this.addRenderableWidget(
                CommonButtons.language(
                        20, button -> this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager())), true
                )
        );
        spriteIconButton.setPosition(SPRITE_XPOS, height - (4 + BUTTON_HEIGHT));
    }

    @ModifyArg(
            method = "init", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/CommonButtons;language(ILnet/minecraft/client/gui/components/Button$OnPress;Z)Lnet/minecraft/client/gui/components/SpriteIconButton;"),
            index = 0
    )
    private int languageButton(int i) {
        return BUTTON_HEIGHT;
    }

    @ModifyArg(
            method = "init", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/CommonButtons;accessibility(ILnet/minecraft/client/gui/components/Button$OnPress;Z)Lnet/minecraft/client/gui/components/SpriteIconButton;"),
            index = 0
    )
    private int accessibilityButton(int i) {
        return BUTTON_HEIGHT;
    }

    @ModifyArgs(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/SpriteIconButton;setPosition(II)V",
                    ordinal = 1
            )
    )
    private void setAccPos(Args args) {
        args.set(0, SPRITE_XPOS + BUTTON_HEIGHT + 4);
        args.set(1, height - (4 + BUTTON_HEIGHT));
    }

    @ModifyArg(
            method = "createNormalMenuOptions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/TitleScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;",
                    ordinal = 0
            )
    )
    private GuiEventListener changeScreen(GuiEventListener par1) {
        int largeButtonWidth = 200;
        int largeButtonXPos = width / 2 - 100;
        int bottomYPos = height / 4 + 48;

        return Button.builder(Component.translatable("menu.singleplayer"),
                (button -> minecraft.setScreen(new SelectWorldScreenSD(this)))
        ).bounds(largeButtonXPos, bottomYPos, largeButtonWidth, BUTTON_HEIGHT).build();
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"),
            cancellable = true
    )
    private void cancelVersion(CallbackInfo ci) {
        ci.cancel();
    }

}
