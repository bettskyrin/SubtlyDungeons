package com.kr1s1s.subtlyd.mixin.client.options;

import net.minecraft.client.resources.SplashManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(SplashManager.class)
public class SplashManagerMixin {
    @Inject(method = "prepare", at = @At("RETURN"), cancellable = true)
    private void appendCustomSplash(ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfoReturnable<List<Component>> cir) {
        List<Component> originalSplashes = cir.getReturnValue();
        List<Component> newSplashes = new ArrayList<>(originalSplashes);

        newSplashes.add(Component.literal("Pretty tents!"));
        newSplashes.add(Component.literal("R.I.P. trout.png"));
        newSplashes.add(Component.literal("L-l-l-lava!"));
        newSplashes.add(Component.literal("Music by Peter Hont!"));
        newSplashes.add(Component.literal("Music by Crispin Hands!!"));
        newSplashes.add(Component.literal("Music by John Johnson!"));
        newSplashes.add(Component.literal("Windy!"));

        cir.setReturnValue(newSplashes);
    }
}
