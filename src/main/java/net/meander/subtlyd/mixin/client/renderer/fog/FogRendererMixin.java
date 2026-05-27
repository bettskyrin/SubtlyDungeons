package net.meander.subtlyd.mixin.client.renderer.fog;

import net.meander.subtlyd.data.tags.BiomeTagsSD;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    private static float fogWeight = 0.0F;
    private static float targetWeight = 0.0F;
    private static long lastCheck = 0;

    @Inject(method = "setupFog", at = @At("RETURN"))
    private static void increaseFog(Camera camera, int renderDistanceInChunks, DeltaTracker deltaTracker, float darkenWorldAmount, ClientLevel level, CallbackInfoReturnable<FogData> cir) {
        long currentTime = level.getGameTime();

        if (currentTime - lastCheck >= 5L || currentTime < lastCheck) {
            targetWeight = getBiomeFogWeight(level.getBiome(camera.blockPosition()));
            lastCheck = currentTime;
        }

        if (fogWeight != targetWeight) {
            float targetFogStart = 0.0F;
            float targetFogEnd = 64.0F;
            FogData fog = cir.getReturnValue();
            fogWeight = Mth.lerp(0.007F, fogWeight, targetWeight);

            fog.renderDistanceStart = Mth.lerp(fogWeight, fog.renderDistanceStart, targetFogStart);
            fog.renderDistanceEnd = Mth.lerp(fogWeight, fog.renderDistanceEnd, targetFogEnd);
        }
    }

    private static float getBiomeFogWeight(Holder<Biome> biome) {
        if (biome.is(BiomeTagsSD.IS_VERY_FOGGY)) {
            return 1.01F;
        } else if (biome.is(BiomeTagsSD.IS_FOGGY)) {
            return 1.0F;
        } else {
            return 0.0F;
        }
    }
}