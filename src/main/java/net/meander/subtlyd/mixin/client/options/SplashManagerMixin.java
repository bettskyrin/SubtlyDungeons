package net.meander.subtlyd.mixin.client.options;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.meander.subtlyd.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(SplashManager.class)
public class SplashManagerMixin {
    /**
     * Fetches splash texts to append to the vanilla list of splashes.
     */
    @Inject(method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Ljava/util/List;",
            at = @At("RETURN"),
            cancellable = true)
    private void appendCustomSplash(ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfoReturnable<List<Component>> cir) {
        List<Component> originalSplashes = cir.getReturnValue();
        List<Component> newSplashes = new ArrayList<>(originalSplashes);
        Identifier splashLocation = Util.identifier("texts/splashes.txt");

        try {
            Optional<Resource> resource = resourceManager.getResource(splashLocation);

            if (resource.isPresent()) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.get().open(), StandardCharsets.UTF_8))) {
                    String line;

                    while ((line = reader.readLine()) != null) {
                        line = line.trim();

                        if (!line.isEmpty()) {
                            newSplashes.add(Component.literal(line).withStyle(ChatFormatting.YELLOW));
                        }
                    }
                }
                Util.log(Component.translatable("argument.resource.splash.loading"));
            }
        } catch (Exception e) {
            Util.log(Component.translatable("argument.resource.splash.not_found", e.getMessage()));
        }

        cir.setReturnValue(newSplashes);
    }
}