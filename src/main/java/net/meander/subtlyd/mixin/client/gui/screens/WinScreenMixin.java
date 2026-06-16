package net.meander.subtlyd.mixin.client.gui.screens;

import net.meander.subtlyd.util.Util;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WinScreen.class)
public abstract class WinScreenMixin {
    @Shadow protected abstract void wrapCreditsIO(Identifier file, WinScreen.CreditsReader creditsReader);
    @Unique private static final Identifier CREDITS = Util.identifier("texts/credits.json");

    @Inject(method = "init",
            at = @At(target = "Lnet/minecraft/client/gui/screens/WinScreen;wrapCreditsIO(Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/gui/screens/WinScreen$CreditsReader;)V",
            value = "INVOKE",
            shift = At.Shift.AFTER,
            ordinal = 1))
    private void addMeanderStudios(CallbackInfo ci) {
        WinScreen screen = (WinScreen) (Object) this;

        wrapCreditsIO(CREDITS, screen::addCreditsFile);
    }
}
