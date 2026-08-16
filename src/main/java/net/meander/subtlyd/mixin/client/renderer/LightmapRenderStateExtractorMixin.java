package net.meander.subtlyd.mixin.client.renderer;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.client.renderer.state.LightmapRenderState;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FogType;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightmapRenderStateExtractor.class)
public class LightmapRenderStateExtractorMixin {
    @Shadow @Final private GameRenderer renderer;
    @Shadow @Final private Minecraft minecraft;
    private float currentDarkness = 1.0F;
    private float prevDarkness = 1.0F;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickDarknessTransition(CallbackInfo ci) {
        float targetDarkness;
        prevDarkness = currentDarkness;

        Camera camera = renderer.mainCamera();
        ClientLevel level = minecraft.level;

        if (level != null && camera.getFluidInCamera() == FogType.WATER && level.getBiome(camera.blockPosition()).is(BiomeTags.IS_OCEAN)) {
            float depth = level.getSeaLevel() - (float) camera.position().y();

            if (depth > 15.0F) {
                float darknessLerp = Mth.clamp((depth - 15.0F) / 30.0F, 0.0F, 1.0F);

                targetDarkness = Mth.lerp(darknessLerp, 1.0F, 0.15F);
            } else {
                targetDarkness = 1.0F;
            }
        } else {
            targetDarkness = 1.0F;
        }

        currentDarkness += (targetDarkness - currentDarkness) * 0.1F;
    }

    @Inject(method = "extract", at = @At("TAIL"))
    private void applyDeepWaterDarkness(LightmapRenderState renderState, float partialTicks, CallbackInfo ci) {
        float multiplier = Mth.lerp(partialTicks, prevDarkness, currentDarkness);

        if (multiplier < 1.0F) {
            renderState.skyFactor *= multiplier;
            renderState.skyLightColor = renderState.skyLightColor.mul(multiplier, new Vector3f());
            renderState.ambientColor = renderState.ambientColor.mul(multiplier, new Vector3f());
            renderState.blockLightTint = renderState.blockLightTint.mul(multiplier, new Vector3f());
        }
    }
}