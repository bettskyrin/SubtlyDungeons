package net.meander.subtlyd.mixin.client.renderer.fog;

import net.meander.subtlyd.tags.BiomeTagsSD;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AtmosphericFogEnvironment.class)
public class FogRendererMixin {
    private static float fogWeight = 0.0F;

    @Inject(method = "setupFog", at = @At("RETURN"))
    private static void increaseFog(FogData fog, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker, CallbackInfo ci) {
        float targetWeight = getAtmosphericFog(level, camera.blockPosition());

        if (fogWeight != targetWeight) {
            fogWeight = Mth.lerp(0.02F * deltaTracker.getGameTimeDeltaPartialTick(true), fogWeight, targetWeight);
        }

        if (fogWeight > 0.001F) {
            float baseStart = fog.environmentalStart;
            float baseEnd = fog.environmentalEnd;
            float targetFogStart = baseStart * 0.10F;
            float targetFogEnd = baseEnd * 0.25F;

            fog.environmentalStart = Mth.lerp(fogWeight, baseStart, targetFogStart);
            fog.environmentalEnd = Mth.lerp(fogWeight, baseEnd, targetFogEnd);
        }
    }

    private static float getAtmosphericFog(ClientLevel level, BlockPos pos) {
        Holder<Biome> biome = level.getBiome(pos);

        if (biome.is(BiomeTagsSD.IS_VERY_FOGGY)) {
            return 1.3F;
        } else if (biome.is(BiomeTagsSD.IS_FOGGY)) {
            return 1.2F;
        } else {
            return 0.0F;
        }
    }
}