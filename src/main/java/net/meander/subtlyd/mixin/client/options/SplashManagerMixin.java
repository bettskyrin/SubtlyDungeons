package net.meander.subtlyd.mixin.client.options;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
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

@Environment(EnvType.CLIENT)
@Mixin(SplashManager.class)
public class SplashManagerMixin {
    /**
     * Appends string literals to the list of splash text.
     */
    @Inject(method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Ljava/util/List;",
            at = @At("RETURN"),
            cancellable = true)
    private void appendCustomSplash(ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfoReturnable<List<Component>> cir) {
        List<Component> originalSplashes = cir.getReturnValue();
        List<Component> newSplashes = new ArrayList<>(originalSplashes);
        List<String> literals = List.of(
                "Pretty tents!",
                "R.I.P. trout.png",
                "L-l-l-lava!",
                "Music by Peter Hont!",
                "Music by Crispin Hands!",
                "Music by John Johnson!",
                "Windy!",
                "We <3 spiders!",
                "Music by Grant Kirkhope!",
                "Also try Minecraft Legends!",
                "Also try Hytale!"
        );

        for (String string : literals) {
            newSplashes.add(Component.literal(string).withStyle(ChatFormatting.YELLOW));
        }
        cir.setReturnValue(newSplashes);
    }
}