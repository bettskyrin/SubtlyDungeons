package com.kr1s1s.subtlyd.mixin.client.options;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.AccessibilityOnboardingScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AccessibilityOnboardingScreen.class)
public class AccessibilityOnboardingScreenMixin {
    @Inject(method = "onClose", at = @At("TAIL"))
    private void setNewTitle(CallbackInfo ci) {
        Minecraft.getInstance().setScreen(new TitleScreen(true));
    }
}
