package com.kr1s1s.subtlyd.mixin.client.options;

import com.kr1s1s.subtlyd.client.OptionsSD;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(AccessibilityOptionsScreen.class)
public class AccessibilityOptionsScreenMixin {
    @Inject(method = "options", at = @At("RETURN"))
    private static OptionInstance<?>[] options(Options options, CallbackInfoReturnable<OptionInstance<?>> cir) {
        return newOptions(options, new OptionsSD(), cir);
    }

    private static OptionInstance<?>[] newOptions(Options options, OptionsSD optionsSD, CallbackInfoReturnable<OptionInstance<?>> cir) {
        return new OptionInstance[]{
                options.narrator(),
                options.showSubtitles(),
                options.highContrast(),
                options.menuBackgroundBlurriness(),
                options.textBackgroundOpacity(),
                options.backgroundForChatOnly(),
                options.chatOpacity(),
                options.chatLineSpacing(),
                options.chatDelay(),
                options.notificationDisplayTime(),
                options.bobView(),
                options.screenEffectScale(),
                options.fovEffectScale(),
                options.darknessEffectScale(),
                options.damageTiltStrength(),
                options.glintSpeed(),
                options.glintStrength(),
                options.hideLightningFlash(),
                optionsSD.doCameraShake(),
                options.darkMojangStudiosBackground(),
                options.panoramaSpeed(),
                options.hideSplashTexts(),
                options.narratorHotkey(),
                options.rotateWithMinecart(),
                options.highContrastBlockOutline()
        };
    }
}
