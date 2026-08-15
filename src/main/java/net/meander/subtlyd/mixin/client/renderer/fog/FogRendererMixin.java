package net.meander.subtlyd.mixin.client.renderer.fog;

import net.meander.subtlyd.client.renderer.entity.OcclusionManager;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Inject(method = "setupFog", at = @At("RETURN"))
    private void setupFog(final Camera camera, final int renderDistanceInChunks, final DeltaTracker deltaTracker, final float darkenWorldAmount, final ClientLevel level, CallbackInfoReturnable<FogData> cir) {
        FogData fogData = cir.getReturnValue();

        setFogForCulling(fogData);
        adjustWaterFogDistance(camera, fogData, level);
    }

    @Inject(method = "computeFogColor", at = @At("RETURN"))
    private void adjustWaterFogColor(final Camera camera, final float partialTicks, final ClientLevel level, final int renderDistance, final float darkenWorldAmount, final Vector4f dest, CallbackInfo ci) {
        if (camera.getFluidInCamera() == FogType.WATER && doesNotHaveNightVision(camera)) {
            float depth = level.getSeaLevel() - (float) camera.position().y();

            if (depth > 15.0F) {
                float darknessLerp = Mth.clamp((depth - 15.0F) / 30.0F, 0.0F, 1.0F); // TODO instead of darker fog, just actually lower the skylight levels...
                float multiplier = Mth.lerp(darknessLerp, 1.0F, 0.3F);

                dest.set(dest.x() * multiplier, dest.y() * multiplier, dest.z() * multiplier, dest.w());
            }
        }
    }

    private void setFogForCulling(FogData fogData) {
        float fogEnd = Math.min(fogData.environmentalEnd, fogData.renderDistanceEnd);

        OcclusionManager.getInstance().setCurrentFogEndSqr(fogEnd);
    }

    private boolean doesNotHaveNightVision(Camera camera) {
        Entity entity = camera.entity();

        return !(entity instanceof LivingEntity livingEntity) || !livingEntity.hasEffect(MobEffects.NIGHT_VISION);
    }

    private void adjustWaterFogDistance(final Camera camera, FogData fogData, final ClientLevel level) {
        if (camera.getFluidInCamera() == FogType.WATER && doesNotHaveNightVision(camera)) {
            float depth = level.getSeaLevel() - (float) camera.position().y();

            if (depth > 15.0F) {
                float opacityLerp = Mth.clamp((depth - 15.0F) / 30.0F, 0.0F, 1.0F);
                float distanceSquash = Mth.lerp(opacityLerp, 1.0F, 0.4F);

                fogData.renderDistanceStart *= distanceSquash;
                fogData.renderDistanceEnd *= distanceSquash;
                fogData.environmentalStart = Math.min(fogData.environmentalStart, fogData.renderDistanceStart);
                fogData.environmentalEnd = Math.min(fogData.environmentalEnd, fogData.renderDistanceEnd);
            }
        }
    }
}